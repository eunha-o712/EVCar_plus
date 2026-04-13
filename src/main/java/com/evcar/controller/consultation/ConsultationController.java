package com.evcar.controller.consultation;

import com.evcar.dto.consultation.ConsultationCreateRequestDto;
import com.evcar.dto.consultation.ConsultationResponseDto;
import com.evcar.repository.vehicle.VehicleRepository;
import com.evcar.service.consultation.ConsultationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultation")
@RequiredArgsConstructor
public class ConsultationController {

    private static final int PAGE_SIZE = 10;

    private final ConsultationService consultationService;
    private final VehicleRepository vehicleRepository;

    @GetMapping
    public String consultationList(@RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                   Model model) {
        List<ConsultationResponseDto> consultationList = consultationService.getConsultationList(keyword);
        PaginationResult<ConsultationResponseDto> paginationResult = paginate(consultationList, page);

        model.addAttribute("consultationList", paginationResult.items());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", paginationResult.currentPage());
        model.addAttribute("totalPages", paginationResult.totalPages());
        model.addAttribute("startPage", paginationResult.startPage());
        model.addAttribute("endPage", paginationResult.endPage());
        model.addAttribute("hasPrevious", paginationResult.hasPrevious());
        model.addAttribute("hasNext", paginationResult.hasNext());

        return "consultation/list";
    }

    @GetMapping("/form")
    public String consultationForm(HttpSession session,
                                   Model model) {
        String loginId = extractLoginId(session);

        model.addAttribute("consultationCreateRequestDto", ConsultationCreateRequestDto.builder()
                .userId(loginId)
                .build());
        model.addAttribute("loginId", loginId);
        model.addAttribute("vehicleList", vehicleRepository.findAllByOrderByBrandAscModelNameAsc());
        return "consultation/form";
    }

    @PostMapping("/form")
    public String createConsultation(@Valid @ModelAttribute ConsultationCreateRequestDto consultationCreateRequestDto,
                                     BindingResult bindingResult,
                                     HttpSession session,
                                     Model model) {
        String loginId = extractLoginId(session);

        if (bindingResult.hasErrors()) {
            model.addAttribute("loginId", loginId);
            model.addAttribute("vehicleList", vehicleRepository.findAllByOrderByBrandAscModelNameAsc());
            return "consultation/form";
        }

        ConsultationCreateRequestDto requestDto = ConsultationCreateRequestDto.builder()
                .userId(loginId)
                .vehicleId(consultationCreateRequestDto.getVehicleId())
                .preferredDatetime(consultationCreateRequestDto.getPreferredDatetime())
                .budget(consultationCreateRequestDto.getBudget())
                .purchasePlan(consultationCreateRequestDto.getPurchasePlan())
                .consultContent(consultationCreateRequestDto.getConsultContent())
                .build();

        consultationService.createConsultation(requestDto);
        return "redirect:/consultation";
    }

    @GetMapping("/{id}")
    public String consultationDetail(@PathVariable("id") String consultId,
                                     Model model) {
        model.addAttribute("consultation", consultationService.getConsultationDetail(consultId));
        return "consultation/detail";
    }

    private String extractLoginId(HttpSession session) {
        Object loginUser = session.getAttribute("loginUser");

        if (loginUser == null) {
            throw new IllegalStateException("로그인 후 이용 가능합니다.");
        }

        try {
            Method getLoginIdMethod = loginUser.getClass().getMethod("getLoginId");
            Object loginId = getLoginIdMethod.invoke(loginUser);

            if (loginId == null || String.valueOf(loginId).isBlank()) {
                throw new IllegalStateException("세션 로그인 아이디가 없습니다.");
            }

            return String.valueOf(loginId);
        } catch (NoSuchMethodException exception) {
            try {
                Method getUserIdMethod = loginUser.getClass().getMethod("getUserId");
                Object userId = getUserIdMethod.invoke(loginUser);

                if (userId == null || String.valueOf(userId).isBlank()) {
                    throw new IllegalStateException("세션 사용자 아이디가 없습니다.");
                }

                return String.valueOf(userId);
            } catch (ReflectiveOperationException innerException) {
                throw new IllegalStateException("loginUser 세션 객체에서 로그인 아이디를 찾을 수 없습니다.", innerException);
            }
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("세션 사용자 정보를 읽는 중 오류가 발생했습니다.", exception);
        }
    }

    private <T> PaginationResult<T> paginate(List<T> source, int page) {
        List<T> safeSource = source == null ? Collections.emptyList() : source;
        int totalCount = safeSource.size();
        int totalPages = Math.max((int) Math.ceil((double) totalCount / PAGE_SIZE), 1);
        int currentPage = Math.min(Math.max(page, 1), totalPages);

        int fromIndex = (currentPage - 1) * PAGE_SIZE;
        int toIndex = Math.min(fromIndex + PAGE_SIZE, totalCount);

        List<T> pageItems = fromIndex >= totalCount
                ? Collections.emptyList()
                : safeSource.subList(fromIndex, toIndex);

        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(totalPages, startPage + 4);
        startPage = Math.max(1, endPage - 4);

        return new PaginationResult<>(
                pageItems,
                currentPage,
                totalPages,
                startPage,
                endPage,
                currentPage > 1,
                currentPage < totalPages
        );
    }

    private record PaginationResult<T>(
            List<T> items,
            int currentPage,
            int totalPages,
            int startPage,
            int endPage,
            boolean hasPrevious,
            boolean hasNext
    ) {
    }
}