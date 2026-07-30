package qrsoft.information.dto.output;

import lombok.Data;
import qrsoft.common.entity.Station;

import java.util.Date;

@Data
public class StationOutput {
	private Integer id;
	private String no;
	private String name;
	private String shortName;
	private Double installCapacity;
	private Integer status;
	private Integer type;
	private Double lon;
	private Double lat;
	private Integer maintainPerson;
	private Integer stationPerson;
	private String constructionPerson;
	private String designPerson;
	private String own;
	private Integer provinceId;
	private Integer cityId;
	private Integer areaId;
	private String address;
	private Integer saleType;
	private Double contractPower;
	private Date contractTime;
	private Double protocolPr;
	private Date netTime;
	private Double avgRadio;
	private Double efficiency;
	private String stationDesc;
	private String componentInfo;
	private Integer buildCycle;
	private Integer joinLevel;
	private String photoPath;
	private Double totalInPower;
	private Double totalOutPower;
	private Double averageEfficiency;

	private SysUserSimpleOutput maintainPersonObj;
	private SysUserSimpleOutput stationPersonObj;
	private ProvinceOutput province;
	private CityOutput city;
	private AreaOutput area;

	public static StationOutput entityToOutput(Station s) {
		if (s == null) return null;
		StationOutput o = new StationOutput();
		o.setId(s.getId());
		o.setNo(s.getNo());
		o.setName(s.getName());
		o.setShortName(s.getShortName());
		o.setInstallCapacity(s.getInstallCapacity());
		o.setStatus(s.getStatus());
		o.setType(s.getType());
		o.setLon(s.getLon());
		o.setLat(s.getLat());
		o.setMaintainPerson(s.getMaintainPerson());
		o.setStationPerson(s.getStationPerson());
		o.setConstructionPerson(s.getConstructionPerson());
		o.setDesignPerson(s.getDesignPerson());
		o.setOwn(s.getOwn());
		o.setProvinceId(s.getProvinceId());
		o.setCityId(s.getCityId());
		o.setAreaId(s.getAreaId());
		o.setAddress(s.getAddress());
		o.setSaleType(s.getSaleType());
		o.setContractPower(s.getContractPower());
		o.setContractTime(s.getContractTime());
		o.setProtocolPr(s.getProtocolPr());
		o.setNetTime(s.getNetTime());
		o.setAvgRadio(s.getAvgRadio());
		o.setEfficiency(s.getEfficiency());
		o.setStationDesc(s.getStationDesc());
		o.setComponentInfo(s.getComponentInfo());
		o.setBuildCycle(s.getBuildCycle());
		o.setJoinLevel(s.getJoinLevel());
		o.setPhotoPath(s.getPhotoPath());
		o.setTotalInPower(s.getTotalInPower());
		o.setTotalOutPower(s.getTotalOutPower());
		o.setAverageEfficiency(s.getAverageEfficiency());
		return o;
	}
}
