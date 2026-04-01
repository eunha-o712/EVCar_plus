package com.evcar.service.wishlist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evcar.domain.vehicle.Vehicle;
import com.evcar.domain.wishlist.Wishlist;
import com.evcar.dto.vehicle.VehicleListDto;
import com.evcar.repository.vehicle.VehicleRepository;
import com.evcar.repository.wishlist.WishlistRepository;

@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

	private final WishlistRepository wishlistRepository;
	private final VehicleRepository vehicleRepository;

	
	public WishlistServiceImpl(WishlistRepository wishlistRepository, VehicleRepository vehicleRepository) {
		this.wishlistRepository = wishlistRepository;
		this.vehicleRepository = vehicleRepository;
	}

	@Override
	public boolean isWished(String vehicleId) {
		return wishlistRepository.existsByVehicleId(vehicleId);
	}

	@Override
	public void add(String vehicleId) {
		if (!wishlistRepository.existsByVehicleId(vehicleId)) {
			Wishlist w = new Wishlist();
			w.setVehicleId(vehicleId);
			wishlistRepository.save(w);
		}
	}

	@Override
	public void remove(String vehicleId) {
		if (wishlistRepository.existsByVehicleId(vehicleId)) {
			wishlistRepository.deleteByVehicleId(vehicleId);
		}
	}

	@Override
	public List<VehicleListDto> getWishlistVehicles() {
		List<Wishlist> wishlist = wishlistRepository.findAll();

		return wishlist.stream().map(w -> {

			// 🔥 var → 명확한 타입으로 변경
			Vehicle vehicle = vehicleRepository.findById(w.getVehicleId()).orElse(null);

			if (vehicle == null)
				return null;

			VehicleListDto dto = new VehicleListDto();
			dto.setBrand(vehicle.getBrand());
			dto.setModelName(vehicle.getModelName());

			return dto;
		}).filter(v -> v != null).collect(Collectors.toList());
	}
}