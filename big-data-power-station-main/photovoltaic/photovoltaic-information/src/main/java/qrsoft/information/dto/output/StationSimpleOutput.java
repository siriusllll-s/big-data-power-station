package qrsoft.information.dto.output;

import lombok.Data;
import qrsoft.common.entity.Station;

@Data
public class StationSimpleOutput {
	private Integer id;
	private String name;
	private String shortName;
	private Double installCapacity;
	private Integer status;
	private Integer type;
	private String own;
	private Integer maintainPerson;
	private Double lon;
	private Double lat;
	private SysUserSimpleOutput maintainPersonObj;

	public static StationSimpleOutput entityToOutput(Station station) {
		StationSimpleOutput out = new StationSimpleOutput();
		if (station == null) {
			out.setId(1);
			out.setName("电站");
			out.setShortName("唯一电站");
			return out;
		}
		out.setId(station.getId());
		out.setName(station.getName());
		out.setShortName(station.getShortName());
		out.setInstallCapacity(station.getInstallCapacity());
		out.setStatus(station.getStatus());
		out.setType(station.getType());
		out.setOwn(station.getOwn());
		out.setMaintainPerson(station.getMaintainPerson());
		out.setLon(station.getLon());
		out.setLat(station.getLat());
		return out;
	}
}
