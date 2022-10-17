package kr.or.mrhi;

import java.util.Date;
import java.util.Objects;

public class Seafood implements Comparable<Object> {

	private final int STANDARD_COUNT = 3;

	private String id;
	private String name;
	private int freshness;
	private int size;
	private int weight;

	private int total;
	private double avg;
	private String grade;
	private int rate;
	private Date date;

	public Seafood(String id, String name, int freshness, int size, int weight) {
		this(id, name, freshness, size, weight, 0, 0.0, null, 0, null);
	}

	public Seafood(String id, String name, int freshness, int size, int weight, int total, double avg, String grade,
			int rate) {
		this(id, name, freshness, size, weight, total, avg, grade, rate, null);
	}

	public Seafood(String id, String name, int freshness, int size, int weight, int total, double avg, String grade,
			int rate, Date date) {
		super();
		this.id = id;
		this.name = name;
		this.freshness = freshness;
		this.size = size;
		this.weight = weight;
		this.total = total;
		this.avg = avg;
		this.grade = grade;
		this.rate = rate;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFreshness() {
		return freshness;
	}

	public void setFreshness(int freshness) {
		this.freshness = freshness;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal() {
		total = (freshness + size + weight);
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg() {
		String avg = String.format("%.2f", (double) (freshness + size + weight) / (double) STANDARD_COUNT);
		this.avg = Double.parseDouble(avg);
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade() {
		int avg = (int) this.avg / 10;

		switch (avg) {
		case 10:
		case 9:
			grade = "A";
			break;
		case 8:
			grade = "B";
			break;
		case 7:
			grade = "C";
			break;
		case 6:
			grade = "D";
			break;
		case 5:
			grade = "E";
			break;
		default:
			grade = "F";
		}
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Seafood)) {
			return false;
		}
		return Objects.equals(this.id, ((Seafood) obj).id);
	}

	@Override
	public String toString() {
		return "id = " + id + "\tname = " + name + "\tfreshness = " + freshness + "\t\tsize = " + size + "\tweight = "
				+ weight + "\ttotal = " + total + "\tavg = " + avg + "\tgrade = " + grade;
	}

	@Override
	public int compareTo(Object obj) {
		Seafood seafood = null;
		if (obj instanceof Seafood) {
			seafood = (Seafood) obj;
		}
		return this.id.compareToIgnoreCase(seafood.id);
	}

}