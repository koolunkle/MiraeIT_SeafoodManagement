package kr.or.mrhi;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class SeafoodService {

	private static final int TOTAL = 0, CURRENT = 1, BEFORE_UPDATE = 2, BEFORE_DELETE = 3;

	private static final int ID = 1, NAME = 2;

	private static List<Seafood> list = new ArrayList<Seafood>();

	public static void statsData() {
		String display = "-".repeat(30) + "\n1. 최고 등급 조회 | 2. 최저 등급 조회\n" + "-".repeat(30) + "\n입력>> ";
		String check = "통계 방식을 다시 입력해 주세요";
		int number = Library.checkException(display, check, Regex.MAX_MIN);

		DBConnection dbCxn = new DBConnection();

		dbCxn.connect();

		list = dbCxn.selectMaxOrMin(number);

		Library.resultPrint(list, CURRENT);

		dbCxn.close();
	}

	public static void sortData() {
		String display = "-".repeat(30) + "\n0. total | 1. id | 2. name\n" + "-".repeat(30) + "\n정렬 기준 선택>> ";
		String check = "정렬 방식을 다시 입력해 주세요";
		int number = Library.checkException(display, check, Regex.ORDER);

		DBConnection dbCxn = new DBConnection();

		dbCxn.connect();

		list = dbCxn.selectOrderBy(number);

		switch (number) {
		case TOTAL:
			Library.resultPrint(list, TOTAL);
			break;

		case ID:
			Library.resultPrint(list, CURRENT);
			break;

		case NAME:
			Library.resultPrint(list, CURRENT);
			break;
		}

		dbCxn.close();
	}

	public static void updateData() {
		int freshness = 0;
		int size = 0;
		int weight = 0;

		String display = "-".repeat(15) + "\n1. id | 2. name\n" + "-".repeat(15) + "\n검색 기준 선택>> ";
		String check = "검색 방식을 다시 입력해 주세요";
		int number = Library.checkException(display, check, Regex.SEARCH);

		DBConnection dbCxn = new DBConnection();

		dbCxn.connect();

		list = Library.checkInput(number, list, dbCxn);

		if (list.size() <= 0) {
			System.out.println("data check fail");
			return;
		}

		Seafood seafood = list.get(0);

		int updatedFreshness = Library.setEvaluation("신선도 재입력 : ", freshness);
		int updatedSize = Library.setEvaluation("크기 재입력 : ", size);
		int updatedWeight = Library.setEvaluation("무게 재입력 : ", weight);

		seafood.setFreshness(updatedFreshness);
		seafood.setSize(updatedSize);
		seafood.setWeight(updatedWeight);

		seafood.setTotal();
		seafood.setAvg();
		seafood.setGrade();

		int value = dbCxn.update(seafood);

		if (value != -1) {
			System.out.println("update success");
		} else {
			System.out.println("update fail");
			return;
		}

		dbCxn.close();
	}

	public static void searchData() {
		String display = "-".repeat(15) + "\n1. id | 2. name\n" + "-".repeat(15) + "\n검색 기준 선택>> ";
		String check = "검색 방식을 다시 입력해 주세요";
		int number = Library.checkException(display, check, Regex.SEARCH);

		DBConnection dbCxn = new DBConnection();

		dbCxn.connect();

		list = Library.checkInput(number, list, dbCxn);

		Library.resultPrint(list, CURRENT);

		dbCxn.close();
	}

	public static void printData() {
		String display = "-".repeat(50) + "\n1. current | 2. before update | 3. before delete\n" + "-".repeat(50)
				+ "\n출력 기준 선택>> ";
		String check = "출력 방식을 다시 입력해 주세요";
		int number = Library.checkException(display, check, Regex.PRINT);

		DBConnection dbCxn = new DBConnection();

		dbCxn.connect();

		switch (number) {
		case CURRENT:
			list = dbCxn.select();
			Library.resultPrint(list, CURRENT);
			break;
		case BEFORE_UPDATE:
			list = dbCxn.selectBeforeUpdate();
			Library.resultPrint(list, BEFORE_UPDATE);
			break;
		case BEFORE_DELETE:
			list = dbCxn.selectBeforeDelete();
			Library.resultPrint(list, BEFORE_DELETE);
			break;
		}

		dbCxn.close();
	}

	public static void deleteData() {
		System.out.print("식별 번호 입력 : ");
		String id = Library.scanner.next();

		boolean isPattern = Library.checkPattern(id, Regex.ID.getValue());

		if (!isPattern) {
			return;
		}

		DBConnection dbCxn = new DBConnection();

		dbCxn.connect();

		int value = dbCxn.delete(id);

		if (value == 0) {
			System.out.println("data check fail");
		}

		if (value == -1) {
			System.out.println("delete fail");
		} else {
			System.out.println("delete success");
		}

		dbCxn.close();
	}

	public static void inputData() {
		boolean isPattern = false;
		int freshness = 0;
		int size = 0;
		int weight = 0;

		System.out.print("식별 번호 입력 : ");
		String id = Library.scanner.next();
		isPattern = Library.checkPattern(id, Regex.ID.getValue());

		if (!isPattern) {
			return;
		}

		System.out.print("이름 입력 : ");
		String name = Library.scanner.next();
		isPattern = Library.checkPattern(name, Regex.NAME.getValue());

		if (!isPattern) {
			return;
		}

		int setFreshness = Library.setEvaluation("신선도 입력 : ", freshness);
		int setSize = Library.setEvaluation("크기 입력 : ", size);
		int setWeight = Library.setEvaluation("무게 입력 : ", weight);

		Seafood seafood = new Seafood(id, name, setFreshness, setSize, setWeight);

		DBConnection dbCxn = new DBConnection();

		dbCxn.connect();

		int value = dbCxn.insert(seafood);

		if (value != -1) {
			System.out.println("insert success");
		} else {
			System.out.println("insert fail");
		}

		dbCxn.close();
	}

	public static int displayMenu() {
		int number = 0;

		System.out.print("-".repeat(70) + "\n1. 입력 | 2. 수정 | 3. 삭제 | 4. 검색 | 5. 출력 | 6. 정렬 | 7. 통계 | 8. 종료\n"
				+ "-".repeat(70) + "\n입력>> ");
		try {
			number = Library.scanner.nextInt();
		} catch (InputMismatchException e) {
			Library.scanner = new Scanner(System.in);
		} catch (Exception e) {
			System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
		}

		return number;
	}

}