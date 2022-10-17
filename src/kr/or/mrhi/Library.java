package kr.or.mrhi;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Library {

	private static final int ID = 1, NAME = 2, EVALUATION = 3, ORDER = 4, MAX_MIN = 5, PRINT = 6, SEARCH = 7;

	private static final int TOTAL = 0, CURRENT = 1, BEFORE_UPDATE = 2, BEFORE_DELETE = 3;

	public static Scanner scanner = new Scanner(System.in);

	public static int setEvaluation(String message, int standard) {
		while (true) {
			System.out.print(message);
			try {
				standard = scanner.nextInt();
				boolean isPattern = checkPattern(String.valueOf(standard), Regex.EVALUATION.getValue());

				if (!isPattern) {
					continue;
				}

				break;
			} catch (InputMismatchException e) {
				System.out.println("정수를 입력해 주세요");
				scanner = new Scanner(System.in);
			} catch (Exception e) {
				System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
			}
		}

		return standard;
	}

	public static List<Seafood> checkInput(int number, List<Seafood> list, DBConnection dbCxn) {
		switch (number) {
		case ID:
			System.out.print("검색 식별 번호 입력>> ");
			String id = Library.scanner.next();
			list = dbCxn.selectIdOrName(id, Regex.ID.getValue());
			break;
		case NAME:
			System.out.print("검색 이름 입력>> ");
			String name = Library.scanner.next();
			list = dbCxn.selectIdOrName(name, Regex.NAME.getValue());
			break;
		}

		return list;
	}

	public static void resultPrint(List<Seafood> list, int number) {
		if (list.size() <= 0) {
			System.out.println("data check fail");
			return;
		}

		switch (number) {
		case CURRENT:
			System.out.println("-".repeat(130));
			for (Seafood seafood : list) {
				System.out.println(seafood);
			}
			System.out.println("-".repeat(130));
			break;

		case BEFORE_UPDATE:
		case BEFORE_DELETE:
			System.out.println("-".repeat(155));
			for (Seafood seafood : list) {
				System.out.println(seafood + "\tdate = " + seafood.getDate());
			}
			System.out.println("-".repeat(155));
			break;

		case TOTAL:
			System.out.println("-".repeat(145));
			for (Seafood seafood : list) {
				System.out.println(seafood + "\trate = " + seafood.getRate());
			}
			System.out.println("-".repeat(145));
			break;
		}
	}

	public static int checkException(String display, String check, Regex regex) {
		int number = 0;
		boolean isPattern = false;

		while (true) {
			System.out.print(display);
			try {
				number = Library.scanner.nextInt();
				isPattern = Library.checkPattern(String.valueOf(number), regex.getValue());

				if (!isPattern) {
					continue;
				}

				break;
			} catch (InputMismatchException e) {
				System.out.println(check);
				Library.scanner = new Scanner(System.in);
			} catch (Exception e) {
				System.out.println(e.getMessage() + "로 인한 " + e.getClass().getName() + " 예외 발생");
			}
		}

		return number;
	}

	public static boolean checkPattern(String attribute, int number) {
		String pattern = null;
		String message = null;

		switch (number) {
		case ID:
			pattern = "^0[1-6]0[1-9][0-6]{2}$";
			message = "식별 번호를 다시 입력해 주세요";
			break;

		case NAME:
			pattern = "^[가-힣]{2,4}$";
			message = "이름을 다시 입력해 주세요";
			break;

		case EVALUATION:
			pattern = "^[0-9]{1,3}$";
			message = "평가 항목을 다시 입력해 주세요";
			break;

		case ORDER:
			pattern = "^[0-2]$";
			message = "정렬 방식을 다시 입력해 주세요";
			break;

		case MAX_MIN:
			pattern = "^[1-2]$";
			message = "통계 방식을 다시 입력해 주세요";
			break;

		case PRINT:
			pattern = "^[1-3]$";
			message = "출력 방식을 다시 입력해 주세요";
			break;

		case SEARCH:
			pattern = "^[1-2]$";
			message = "검색 방식을 다시 입력해 주세요";
			break;
		}

		boolean regex = Pattern.matches(pattern, attribute);

		if (number == EVALUATION) {
			if (!regex || Integer.parseInt(attribute) < 0 || Integer.parseInt(attribute) > 100) {
				System.out.println(message);
				return false;
			}
		}

		if (!regex) {
			System.out.println(message);
			return false;
		}

		return true;
	}

}