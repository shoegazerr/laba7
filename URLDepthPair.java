// класс для пары ссылка глубина
public class URLDepthPair{
	// делаю константы, чтоб сам не смог поменять
	private final String URL;
	// это как надеть на себя смирящую рубашку
	private final int DEPTH;

	// или как там эта рубашка называется
	public URLDepthPair(){
		this.URL = "";
		this.DEPTH = 0;
	}
	// в нее сумасшедших одевают
	public URLDepthPair(String url, int depth){
		this.URL = url;
		this.DEPTH = depth;
	} 
	// кстати, вроде везде правильно использовал
	// надеть и одеть
	public String getUrl(){
		return this.URL;
	}
	// проверять я конечно же не буду
	public int getDepth(){
		return this.DEPTH;
	}
	// крч если есть ссылка, то она поступает сюда
	// следующего вида: urlMainName/subname/another/...
	// этот метод обрежет всё ненужное
	// и для ссылки из примера оставит только urlMainName
	// http:// и подобное обрезается заранее
	public String getMainURLPage(){
		return this.getUrl().split("/")[0];
	}

	// эа штука вернет только доп часть ссылки я хз как назыввается 
	// если знаешь как это называется, напиши мне 89519822250
	// гуглить сам я конечно же не буду
	// для всё той же примерочной ссылки вернет /subname/another/...
	public String getAfterMainURLPage(){
		// делю ссылку по /
		String[] urlsParts = this.getUrl().split("/");
		// если в ссылке нет /, то вернет просто /
		if (urlsParts.length < 2)
			return "/";
		else{
			// сначала получаю основную часть ссылки
			String res = this.getUrl().substring(urlsParts[0].length());
			// если есть продолжение, то обрезаю основную часть ссылки
			if (res.endsWith("/"))
				res = res.substring(0, res.length());
			// иначе возвращаю то, что есть
			// хотя вроде этот ретёрн не должен возвращать
			return res;
		}
	}
	// если есть пара глубины 6 и какимто юрлом,
	// то вернется "      6 : какой-то юрл" (там до 6 - 6 пробелов)
	public String toString(){
		return " ".repeat(this.DEPTH) + this.DEPTH + " : " + this.URL;
	}
}