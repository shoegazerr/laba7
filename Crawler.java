import java.net.*;
import java.io. *;
// остался последний класс, фууух
// эта штука будет подключаться к серверу по ссылке
// и получать весь хтмл код, обрабатывать его
// и возвращать ссылочки в нужном формате
// даже regex не нужны
public class Crawler{
	// сохраню сюда сокет
	private Socket socket;
	// а сюда читалку инфы приходящей с сервера
	private BufferedReader in;
	// эта штука будет отправлять на сервер любовные послания
	private PrintWriter out;
	// флажок для чтения строк хтмл, если хтмл закончился, то фолс
	private boolean inReadLineAvailable = true;
	// главная пара, куда же без нее
	// но это главная пара не та главная, которая там
	// в каждый crawler поступает пара ссылки и глубины, и любая сслылка сохраняется как main
	// лень переписывать название
	private URLDepthPair mainPair;

	// сюда позже вернусь, выглядит страшно
	// здесь запускается crawwler (паучок)
	public void start(URLDepthPair mainPair){
		try { // трай кэтч а вдруг что-то сломается
			// System.out.println("(" + mainPair.getAfterMainURLPage() + ") (" + mainPair.getMainURLPage() + ")");
			this.mainPair = mainPair;
			socket = new Socket();
			// ставлю ожидание меньше 500 миллисек
			socket.setSoTimeout(500);
			// коннекчусь к серваку
			socket.connect(new InetSocketAddress(mainPair.getMainURLPage(), 80), 500);
			// получаю получалку
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// получаю отдавалку
			out = new PrintWriter(socket.getOutputStream(), true);
			// говорю серверу, что я добрый, сервер поймет что мне можно
			// доверять и тогда я стыру у него весь хтмл
			// ХА-ХА-ХА какой я коварный
			// запрос выглядит как-то так
			// GET /about HTTP/1.1
			out.println("GET " + mainPair.getAfterMainURLPage() + " HTTP/1.1");
			// Host: tapakahokot.ru
			// тоесть в GET кидаем доп часть ссылки, а в Host основую
			// я хз почему так надо, вычисленно методом проб и страданий
            out.println("Host: " + mainPair.getMainURLPage());
            // это тоже вроде нужно
            out.println("Accept: */*");
            out.println("User-Agent: Java");
            out.println(""); // отправить пустую строку - самое важное, так мы 
            // даем понять, что ввод закончен
            // а это наверное пуш ввода на сервак
            out.flush();
			// System.out.println("\n\n<<< Socket is running >>>");

		} catch (Exception e){
			// System.out.println(e);
		}
	}

	// нет, лучше сначала то что сверху распишу
	// эта штука буде читать все строки
	// визуально выглядит ужасно
	// переписывать лень
	public String readLine(){
		try{
			// тру, пока есть хтмл код
			if (inReadLineAvailable){
				// читаем следующую строку хтмл
				String line = in.readLine();
				if (line == null){
					// если она null, то прекращаем чтение
					inReadLineAvailable = false;
					return null;
				}
				return line;
			}
			else
				return null;
		}
		catch (Exception e){
			// System.out.println(e);
			return null;
		}
	}

	// чистим ссылку от хлама
	public String clearHref(String line){
		// System.out.println(line);
		String res = "";
		// проверяю, есть ли в ссылке href=" или href=', если есть,
		// то удаляю хреф=кавычка и еще удаляю кавычку в конце ссылки
		if (line.contains("href=\""))
			res = line.split("href=\"")[1].split("\"")[0];
		else if (line.contains("href='"))
			res = line.split("href=\'")[1].split("\'")[0];
		// если ссылка - хттп, то норм, возвращаю сразу, но без хттп
		if (res.startsWith("http://"))
			return res.substring(7);
		else // иначе если ссылка выглядит так /about, то добавляю к ней
			// основную часть ссылки и возвращаю tapakahokot.ru/about
			if (res.startsWith("/"))
				return this.mainPair.getMainURLPage() + res;
			// если это ссылка хттпс или еще что-то, то просто верну ничего
			return "";
	}

	// получаю следующую ссылку
	public String nextUrl(){
		String line;
		// кручу цикл, получая каждый раз новую строку хтмл
		// пока не встречу href в строке кода
		while ( (line = this.readLine()) != null ){
			line = line.toLowerCase(); // хтмл может быть написан капсом
			// System.out.println(line);
			if (line.contains("href="))
				return clearHref(line);
		}
		// нул если ниче нет
		return null;
	}	
	// закрываю сокет, ввод и вывод
	public void close(){
		try{
			in.close();
			out.close();
			socket.close();
			// System.out.println("<<< Socket is closed >>>\n\n");
		}
		catch (Exception e){
			// System.out.println(e);
		}
	}
	// конструктор по умолчанию
	public Crawler(){
		// System.out.println("Created");
	}
}