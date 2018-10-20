package com.crossover.techtrial.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.crossover.techtrial.dto.TopDriverDTO;

@Entity
@Table(name = "ride")
@SqlResultSetMapping(name = "TopDrivers", classes = @ConstructorResult(targetClass = TopDriverDTO.class, columns = {
		@ColumnResult(name = "name"), @ColumnResult(name = "email"),
		@ColumnResult(name = "totalRideDurationInSeconds", type = Long.class),
		@ColumnResult(name = "maxRideDurationInSecods", type = Long.class),
		@ColumnResult(name = "averageDistance", type = Double.class) }))
@NamedNativeQuery(name = "Ride.findTopDrivers", query = "SELECT person.name, person.email, MAX(durationSum) AS totalRideDurationInSeconds, "
		+ "maxRideDurationInSecods, averageDistance " + "FROM ("
		+ "SELECT driver_id, SUM(TIMESTAMPDIFF(second, start_time, end_time)) AS durationSum, "
		+ "MAX(TIMESTAMPDIFF(second, start_time, end_time)) AS maxRideDurationInSecods, "
		+ "AVG(distance + 0.0) AS averageDistance " + "FROM ride " + "WHERE (start_time >= ?1 AND end_time < ?2) "
		+ "GROUP BY driver_id" + ") AS subRide " + "INNER JOIN person ON driver_id = person.id " + "GROUP BY driver_id "
		+ "ORDER BY MAX(durationSum) DESC", resultSetMapping = "TopDrivers")
public class Ride implements Serializable {

	private static final long serialVersionUID = 9097639215351514001L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "start_time", nullable = false)
	private String startTime;

	@Column(name = "end_time", nullable = false)
	private String endTime;

	@Column(name = "distance")
	private Long distance;

	@ManyToOne
	@JoinColumn(name = "driver_id", referencedColumnName = "id")
	private Person driver;

	@ManyToOne
	@JoinColumn(name = "rider_id", referencedColumnName = "id")
	private Person rider;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Person getDriver() {
		return driver;
	}

	public void setDriver(Person driver) {
		this.driver = driver;
	}

	public Person getRider() {
		return rider;
	}

	public void setRider(Person rider) {
		this.rider = rider;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ride other = (Ride) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
