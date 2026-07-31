package qrsoft.information.dto.input;

import lombok.Data;
import qrsoft.common.entity.Station;
import qrsoft.common.util.DateUtil;

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
	private Integer saleType;
	private Integer buildCycle;
	private String stationDesc;
	/** yyyy-MM-dd */
	private String netTime;
	private Double protocolPr;
	private Double efficiency;
	private Double avgRadio;
	private Double contractPower;
	private Integer provinceId;
	private Integer cityId;
	private Integer areaId;
	private Integer maintainPerson;
	private Integer stationPerson;

	public static void inputToEntity(Station station, StationInput input) {
		if (station == null || input == null) {
			return;
		}
		if (input.getName() != null) {
			station.setName(input.getName());
		}
		if (input.getShortName() != null) {
			station.setShortName(input.getShortName());
		}
		if (input.getInstallCapacity() != null) {
			station.setInstallCapacity(input.getInstallCapacity());
		}
		if (input.getStatus() != null) {
			station.setStatus(input.getStatus());
		}
		if (input.getType() != null) {
			station.setType(input.getType());
		}
		if (input.getLon() != null) {
			station.setLon(input.getLon());
		}
		if (input.getLat() != null) {
			station.setLat(input.getLat());
		}
		if (input.getAddress() != null) {
			station.setAddress(input.getAddress());
		}
		if (input.getOwn() != null) {
			station.setOwn(input.getOwn());
		}
		if (input.getSaleType() != null) {
			station.setSaleType(input.getSaleType());
		}
		if (input.getBuildCycle() != null) {
			station.setBuildCycle(input.getBuildCycle());
		}
		if (input.getStationDesc() != null) {
			station.setStationDesc(input.getStationDesc());
		}
		if (input.getProtocolPr() != null) {
			station.setProtocolPr(input.getProtocolPr());
		}
		if (input.getEfficiency() != null) {
			station.setEfficiency(input.getEfficiency());
		}
		if (input.getAvgRadio() != null) {
			station.setAvgRadio(input.getAvgRadio());
		}
		if (input.getContractPower() != null) {
			station.setContractPower(input.getContractPower());
		}
		if (input.getProvinceId() != null) {
			station.setProvinceId(input.getProvinceId());
		}
		if (input.getCityId() != null) {
			station.setCityId(input.getCityId());
		}
		if (input.getAreaId() != null) {
			station.setAreaId(input.getAreaId());
		}
		if (input.getMaintainPerson() != null) {
			station.setMaintainPerson(input.getMaintainPerson());
		}
		if (input.getStationPerson() != null) {
			station.setStationPerson(input.getStationPerson());
		}
		if (input.getNetTime() != null && !input.getNetTime().isEmpty()) {
			try {
				station.setNetTime(DateUtil.stringToDate(input.getNetTime().substring(0, Math.min(10, input.getNetTime().length())), DateUtil.YYMMDD));
			} catch (Exception ignored) {
				// keep old value
			}
		}
	}
}
