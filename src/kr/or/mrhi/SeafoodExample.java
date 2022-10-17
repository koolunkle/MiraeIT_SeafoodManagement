package kr.or.mrhi;

public class SeafoodExample {

	private static final int INPUT = 1, UPDATE = 2, DELETE = 3, SEARCH = 4;
	private static final int PRINT = 5, SORT = 6, STATS = 7, EXIT = 8;

	public static void main(String[] args) {
		boolean stopFlag = false;

		while (!stopFlag) {
			switch (SeafoodService.displayMenu()) {
			case INPUT:
				SeafoodService.inputData();
				break;

			case UPDATE:
				SeafoodService.updateData();
				break;

			case DELETE:
				SeafoodService.deleteData();
				break;

			case SEARCH:
				SeafoodService.searchData();
				break;

			case PRINT:
				SeafoodService.printData();
				break;

			case SORT:
				SeafoodService.sortData();
				break;

			case STATS:
				SeafoodService.statsData();
				break;

			case EXIT:
				stopFlag = true;
				break;

			default:
				System.out.println("1~8 중 하나를 입력해 주세요");
			}
		}

		System.out.println("프로그램 종료");
	}

}