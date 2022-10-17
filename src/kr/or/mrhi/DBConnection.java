package kr.or.mrhi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class DBConnection {

	private static final int TOTAL = 0, ID = 1, NAME = 2;
	private static final int MAX_TOTAL = 1, MIN_TOTAL = 2;

	private Connection connection = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	// Connect Database
	public void connect() {
		Properties properties = getProperties();
		try {
			Class.forName(properties.getProperty("driver"));
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}
	}

	// Select Statement
	public List<Seafood> select() {
		List<Seafood> list = new ArrayList<Seafood>();
		String query = "SELECT * FROM seafood";

		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery(query);

			if (!(resultSet != null || resultSet.isBeforeFirst())) {
				return list;
			}

			while (resultSet.next()) {
				String id = resultSet.getString("id");
				String name = resultSet.getString("naming");
				int freshness = resultSet.getInt("freshness");
				int size = resultSet.getInt("size");
				int weight = resultSet.getInt("weight");

				int total = resultSet.getInt("total");
				double avr = resultSet.getDouble("avr");
				String grade = resultSet.getString("grade");
				int rate = resultSet.getInt("rate");

				list.add(new Seafood(id, name, freshness, size, weight, total, avr, grade, rate));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}

		return list;
	}

	// Select id, name Statement
	public List<Seafood> selectIdOrName(String attribute, int type) {
		List<Seafood> list = new ArrayList<Seafood>();
		String pattern = "%" + attribute + "%";
		String query = "SELECT * FROM seafood WHERE ";

		switch (type) {
		case ID:
			query += "id LIKE ?";
			break;

		case NAME:
			query += "naming LIKE ?";
			break;

		default:
			System.out.println("1 or 2 입력");
			return list;
		}

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, pattern);
			resultSet = preparedStatement.executeQuery();

			if (!(resultSet != null || resultSet.isBeforeFirst())) {
				return list;
			}

			while (resultSet.next()) {
				String id = resultSet.getString("id");
				String name = resultSet.getString("naming");
				int freshness = resultSet.getInt("freshness");
				int size = resultSet.getInt("size");
				int weight = resultSet.getInt("weight");

				int total = resultSet.getInt("total");
				double avr = resultSet.getDouble("avr");
				String grade = resultSet.getString("grade");
				int rate = resultSet.getInt("rate");

				list.add(new Seafood(id, name, freshness, size, weight, total, avr, grade, rate));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}

		return list;
	}

	// Select Order By id, name, total Statement
	public List<Seafood> selectOrderBy(int type) {
		List<Seafood> list = new ArrayList<Seafood>();
		String query = "SELECT * FROM seafood ORDER BY ";

		switch (type) {
		case TOTAL:
			query += "total DESC";
			break;

		case ID:
			query += "id ASC";
			break;

		case NAME:
			query += "naming ASC";
			break;

		default:
			System.out.println("0, 1, 2 중 하나 입력");
			return list;
		}

		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			if (!(resultSet != null || resultSet.isBeforeFirst())) {
				return list;
			}

			int rank = 0;

			while (resultSet.next()) {
				String id = resultSet.getString("id");
				String name = resultSet.getString("naming");
				int freshness = resultSet.getInt("freshness");
				int size = resultSet.getInt("size");
				int weight = resultSet.getInt("weight");

				int total = resultSet.getInt("total");
				double avr = resultSet.getDouble("avr");
				String grade = resultSet.getString("grade");

				int rate = resultSet.getInt("rate");
				if (type == TOTAL) {
					rate = ++rank;
					// TODO : database update
				}

				list.add(new Seafood(id, name, freshness, size, weight, total, avr, grade, rate));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}

		return list;
	}

	// Select MAX, MIN Statement
	public List<Seafood> selectMaxOrMin(int type) {
		List<Seafood> list = new ArrayList<Seafood>();
		String query = "SELECT * FROM seafood WHERE total = ";

		switch (type) {
		case MAX_TOTAL:
			query += "(SELECT getMaxTotal())";
			break;

		case MIN_TOTAL:
			query += "(SELECT getMinTotal())";
			break;

		default:
			System.out.println("1 or 2 입력");
			return list;
		}

		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			if (!(resultSet != null || resultSet.isBeforeFirst())) {
				return list;
			}

			while (resultSet.next()) {
				String id = resultSet.getString("id");
				String name = resultSet.getString("naming");
				int freshness = resultSet.getInt("freshness");
				int size = resultSet.getInt("size");
				int weight = resultSet.getInt("weight");

				int total = resultSet.getInt("total");
				double avr = resultSet.getDouble("avr");
				String grade = resultSet.getString("grade");
				int rate = resultSet.getInt("rate");

				list.add(new Seafood(id, name, freshness, size, weight, total, avr, grade, rate));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}

		return list;
	}

	// Before Update Trigger
	public List<Seafood> selectBeforeUpdate() {
		List<Seafood> list = new ArrayList<Seafood>();
		String query = "SELECT * FROM update_seafood;";

		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery(query);

			if (!(resultSet != null || resultSet.isBeforeFirst())) {
				return list;
			}

			while (resultSet.next()) {
				String id = resultSet.getString("id");
				String naming = resultSet.getString("naming");
				int freshness = resultSet.getInt("freshness");
				int size = resultSet.getInt("size");
				int weight = resultSet.getInt("weight");

				int total = resultSet.getInt("total");
				double avr = resultSet.getDouble("avr");
				String grade = resultSet.getString("grade");
				int rate = resultSet.getInt("rate");
				Date updatedTime = resultSet.getDate("updated_time");

				list.add(new Seafood(id, naming, freshness, size, weight, total, avr, grade, rate, updatedTime));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}

		return list;
	}

	// Before Delete Trigger
	public List<Seafood> selectBeforeDelete() {
		List<Seafood> list = new ArrayList<Seafood>();
		String query = "SELECT * FROM delete_seafood;";

		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery(query);

			if (!(resultSet != null || resultSet.isBeforeFirst())) {
				return list;
			}

			while (resultSet.next()) {
				String id = resultSet.getString("id");
				String naming = resultSet.getString("naming");
				int freshness = resultSet.getInt("freshness");
				int size = resultSet.getInt("size");
				int weight = resultSet.getInt("weight");

				int total = resultSet.getInt("total");
				double avr = resultSet.getDouble("avr");
				String grade = resultSet.getString("grade");
				int rate = resultSet.getInt("rate");
				Date deletedTime = resultSet.getDate("deleted_time");

				list.add(new Seafood(id, naming, freshness, size, weight, total, avr, grade, rate, deletedTime));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}

		return list;
	}

	// Insert Statement
	public int insert(Seafood seafood) {
		int value = -1;
		String query = "call insert_data(?, ?, ?, ?, ?)";

		try {
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setString(1, seafood.getId());
			preparedStatement.setString(2, seafood.getName());
			preparedStatement.setInt(3, seafood.getFreshness());
			preparedStatement.setInt(4, seafood.getSize());
			preparedStatement.setInt(5, seafood.getWeight());

			value = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}

		return value;
	}

	// Update Statement
	public int update(Seafood seafood) {
		int value = -1;
		String query = "call update_data(?, ?, ?, ?)";

		try {
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setString(1, seafood.getId());
			preparedStatement.setInt(2, seafood.getFreshness());
			preparedStatement.setInt(3, seafood.getSize());
			preparedStatement.setInt(4, seafood.getWeight());

			value = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}

		return value;
	}

	// Delete Statement
	public int delete(String id) {
		int value = -1;
		String query = "DELETE FROM seafood WHERE id = ?";

		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, id);
			value = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}

		return value;
	}

	// Close Connection
	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}
	}

	// Properties
	public Properties getProperties() {
		Properties properties = new Properties();
		try {
			InputStream fis = new FileInputStream(
					"C:/Users/Public/eclipse-workspace/SeafoodManagement/src/kr/or/mrhi/db.properties");
			properties.load(fis);
		} catch (IOException e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}
		return properties;
	}

}