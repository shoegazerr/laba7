import java.util.LinkedList;
// тут храню ссылки, которые уже проходил
// нужно статичное поле, чтобы к нему имели доступ 
// сразу все потоки и кадый мог записать еще новую ссылку
// вроде это не очень надежно, но кого это волнует
public class ThreadList{
	// тот самый список с уникальными ссылками (точнее хэшкодами ссылок)
	public static volatile LinkedList<Integer> listURLS = new LinkedList<Integer>();
	// а тут я упоролся и решил сохранить ссылки на hhtp ресурсы не в блокнот,
	// а сразу в прогу, в тестовых вызовах достаю ссылки именно отсюда
	public static String[] urlsList = {
		"government.ru/",
		"www.garant.ru/",
		"www.consultant.ru/",
		"fish.gov.ru/",
		"pravo.gov.ru/",
		"admoblkaluga.ru/main/"
	};
}