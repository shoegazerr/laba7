import java.util.LinkedList;
import java.util.Scanner;
import java.util.Arrays;

// Эта хрень будет накапливать найденные ссылки
// в другую хрень из разных потоков
// кстати implements Runnable нужно, чтобы 
// ниже запустить объект этого класса в потоке
public class FindAllHrefs implements Runnable{
	// вот в эту хрень накапливаются все ссылки
	private LinkedList<URLDepthPair> list = new LinkedList<URLDepthPair>();
	// тут сохранится максимальная глубина поиска
	private int maxDepth;
	// а тут прям по названию будет храниться главная, "начальная" пара
	private URLDepthPair mainPair;

	// лего
	public FindAllHrefs(int maxDepth, URLDepthPair pair){
		this.maxDepth = maxDepth;
		mainPair = pair;
		list.add(pair);
	}
	// лего, только умолчанее
	public FindAllHrefs(){
		this.maxDepth = 0;
	}

	// переопределил run для реализации интерфейса
	@Override
	public void run(){
		this.saveToListAllLinkes(mainPair);
	}
	// оставил эту штуку напоследок
	// ммм, вкусна
	public void saveToListAllLinkes(URLDepthPair pair){
		// проверочка, не ушел ли я слишком глубоко
		if (pair.getDepth() < this.maxDepth){
			// System.out.println("\nCurrent pair: " + pair.toString());

			// создаю нового паучка, который отыщет по данной 
			// ссылке все новые ссылки
			Crawler crawler = new Crawler();
			crawler.start(pair);

			// заранее определяюю по фану
			URLDepthPair cPair;
			String linkUrl;
			// пока мой метод некстЮрл не вернет нулл, кручу цикл
			// некст юрл возвращает следующую ссылку, если ссылки
			// закончились то он напишет, что о вас думает и вернет null
			while ( (linkUrl = crawler.nextUrl()) != null ){
				// если ссылка не подходит по формату, чтобы не ломался
				// весь цикл, возвращается пустая строка, а не null
				if (!linkUrl.equals("")){
					// System.out.println(linkUrl);
					// создаю пару на основе новой ссылки и глубины
					cPair = new URLDepthPair(linkUrl, pair.getDepth() + 1);
					// а тут получаю хэшКод ссылки новой пары
					int pairHash = cPair.getUrl().hashCode(); 
					// если крч эта ссылка уникальная, то 
					// я ее просматриваю
					if (!ThreadList.listURLS.contains(pairHash)){
						// сохраняю хэшкод каждой новой ссылки
						// чтобы ее больше не вывело
						ThreadList.listURLS.add(pairHash);
						// а тут просто сохраняю пару
						list.add(cPair);
						// иду в глубину для текущей ссылки
						// ооо да, это же рекурсия
						// 	ооо да, это же рекурсия
						// 		ооо да, это же рекурсия
						this.saveToListAllLinkes(cPair);
						// фууух, я думал будет тяжелее
						// осталось описать еще 4 класса
					}
				}
			}
			crawler.close();
		}
	}

	// прохожусь по всему, что насохронял и вывожу через паир.туСтринг
	public void printList(){
		for (URLDepthPair pair : list){
			System.out.println(pair.toString());
		}
	}
	// получить колво накопленных ссылок
	// нужно будет только в одном месте
	public int getListSize(){
		return list.size();
	}

	// а это вообще не помню нафиг надо, 
	// просто получаю списко накопленных ссылок
	// а, вспомнил
	public LinkedList<URLDepthPair> getList(){
		return list;
	}

	// наконец-то дошел до майн...крафт
	public static void main(String[] args){
		// тупо ввод данных
		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter URL-adress: ");
		String url = scanner.nextLine();

		System.out.println("Enter scanner depth: ");
		int depth = scanner.nextInt();
		// если вдруг юзер ввел что хочет глубину -199
		depth = depth < 1 ? 1 : depth;
		// тестовый ввод, если лень писать ручками
		// int depth = 5;
		// String url = ThreadList.urlsList[1];

		// главная пара лол
		URLDepthPair mainPair = new URLDepthPair(url, 0);
		// главный поискоик лол
		FindAllHrefs finder = new FindAllHrefs(1, mainPair);
		// сначала ищу ссылки в глубину 1, н еважно что ввел юзер
		finder.saveToListAllLinkes(mainPair);
		// finder.printList();
		// finder и thread массивы
		// тут будут храниться ссылки глубины 1
		var finderArr = new FindAllHrefs[finder.getListSize() - 1];
		// а ту потоки сслок глубины 1
		var threads = new Thread[finder.getListSize() - 1];
		// заполняем finder и thread массивы
		int counter = 0;
		for (var pair : finder.getList()){
			if (pair.getDepth() != 0){
				finderArr[counter] = new FindAllHrefs(depth, pair);
				threads[counter] = new Thread(finderArr[counter]);
				counter++;
			}

		}
		// о, а эти комменты я сам написал
		// для каждой ссылки на глубине 1 создаем свой поток
		// который идет вглубь до введенного юзером значения
		// работаем с потоками
		System.out.println("\n\n<<" + threads.length + ">>\n\n");
		System.out.println(mainPair.toString());
		try{
			// запускаем потоки
			for (var th : threads)
				th.start();
			// ловим остановки потоков
			for (var th : threads)
				th.join();
			// выводим результаты
			for (var fin : finderArr)
				fin.printList();
		} catch (Exception e){}
	}
}