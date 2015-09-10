import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;


public class ExecutorCrawler {

	private static boolean isValidURL(String url) {  

	    URL u = null;

	    try {  
	        u = new URL(url);  
	    } catch (MalformedURLException e) {  
	        return false;  
	    }

	    try {  
	        u.toURI();  
	    } catch (URISyntaxException e) {  
	        return false;  
	    }  

	    return true;  
	} 
	
	public static void main(String[] args) {
		
		if(isValidURL(args[0]))
		{
		
			String inputParam = args[0];
			
			MainCrawler mainCrawler = MainCrawler.getInstance();
			mainCrawler.setDomain(inputParam);
			
			mainCrawler.linkCrawled.add(inputParam);
			System.out.println("Sitemap Printing");
			System.out.println(inputParam);
			mainCrawler.crawlURL(inputParam,1);
			
			System.out.println("Source Printing");
			Iterator itSrcPrint = mainCrawler.srcCrawled.iterator();
			
			while(itSrcPrint.hasNext())
			{
				String item = (String)itSrcPrint.next();
				System.out.println(item);
			}
			
		} else
		{
			System.out.println("Please enter valid URL");
		}
		
		

	}

}
