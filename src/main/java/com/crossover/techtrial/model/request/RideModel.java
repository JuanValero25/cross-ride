package com.crossover.techtrial.model.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RideModel {

	@NotBlank
	private String startTime;

	@NotBlank
	private String endTime;

	@Min(value = 0)
	private Long distance;

	@NotNull
	private Long riderId;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	public Long getRiderId() {
		return riderId;
	}

	public void setRiderId(Long riderId) {
		this.riderId = riderId;
	}

	@Override
	public String toString() {
		return "RideModel [startTime=" + startTime + ", endTime=" + endTime + ", distance=" + distance + ", riderId="
				+ riderId + "]";
	}
}
