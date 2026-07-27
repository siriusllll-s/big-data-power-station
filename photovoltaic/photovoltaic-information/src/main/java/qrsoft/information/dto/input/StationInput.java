package qrsoft.information.dto.input;

import lombok.Data;
import qrsoft.common.entity.Station;

@Data
public class StationInput {
	private Integer id;
	private String name;
	private String shortName;
	private Double installCapacity;
	private Integer status;
	private Integer type;
	private Double lon;
	private Double lat;
	private String address;
	private String own;

	public static void inputToEntity(Station station, StationInput input) {
		if (station == null || input == null) return;
		station.setName(input.getName());
		station.setShortName(input.getShortName());
		station.setInstallCapacity(input.getInstallCapacity());
		station.setStatus(input.getStatus());
		station.setType(input.getType());
		station.setLon(input.getLon());
		station.setLat(input.getLat());
		station.setAddress(input.getAddress());
		station.setOwn(input.getOwn());
	}
}
