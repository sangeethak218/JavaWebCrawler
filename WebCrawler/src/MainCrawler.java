import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class MainCrawler {

	private Pattern patternTag, patternLink;
	private Matcher matcherTag, matcherLink;
	
	private Pattern patternSrcLink;
	private Matcher matcherSrcLink;
	
	private static MainCrawler mainCrawler;
	private String domain;
	protected Set<String> linkCrawled;
	protected Set<String> srcCrawled;
	
	private static final String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
	private static final String HTML_A_HREF_TAG_PATTERN = 
		"\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
	
	private static final String HTML_SRC_TAG_PATTERN = 
			"\\s*(?i)src\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
		
	private MainCrawler() {
		patternTag = Pattern.compile(HTML_A_TAG_PATTERN);
		patternLink = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
		patternSrcLink = Pattern.compile(HTML_SRC_TAG_PATTERN);
		
		linkCrawled = new HashSet<String>();
		srcCrawled = new HashSet<String>();
	}
	
	public static MainCrawler getInstance(){
		if(mainCrawler==null)
		{
			mainCrawler = new MainCrawler();
		}
		return mainCrawler;
	}
	
	public void setDomain(String domain){
		this.domain = domain;
	}
	
	
	
	
	public Vector<HtmlLink> grabHTMLSrcLinks(final String html) {

		Vector<HtmlLink> result = new Vector<HtmlLink>();

			matcherSrcLink = patternSrcLink.matcher(html);

			while (matcherSrcLink.find()) {

				String link = matcherSrcLink.group(1);
				HtmlLink obj = new HtmlLink();
				obj.setLink(link);
				
				result.add(obj);

			}

		return result;

	}
	
	public Vector<HtmlLink> grabHTMLLinks(final String html) {

		Vector<HtmlLink> result = new Vector<HtmlLink>();

		matcherTag = patternTag.matcher(html);

		while (matcherTag.find()) {

			String href = matcherTag.group(1); 
			String linkText = matcherTag.group(2); 

			matcherLink = patternLink.matcher(href);

			while (matcherLink.find()) {

				String link = matcherLink.group(1); 
				HtmlLink obj = new HtmlLink();
				obj.setLink(link);
				obj.setLinkText(linkText);
				
				result.add(obj);

			}

		}

		return result;

	}
	
	public void crawlURL(String paramURL, int level)
	{
		try {
			URL url = new URL(paramURL);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String strTemp = "";
			String completeHTML = "";
			while (null != (strTemp = br.readLine())) {
				completeHTML += strTemp;
			}

			Vector<HtmlLink> resultLink = mainCrawler.grabHTMLLinks(completeHTML);
			Vector<HtmlLink> resultSrc = mainCrawler.grabHTMLSrcLinks(completeHTML);
			
			
			Iterator itSrc = resultLink.iterator();
			
			while(itSrc.hasNext())
			{
				HtmlLink item = (HtmlLink)itSrc.next();
				if(item.getLink()!=null && item.getLink().contains(domain) && !srcCrawled.contains(item.getLink()))
				{
					srcCrawled.add(item.getLink());
				}
			}
			
			Iterator it = resultLink.iterator();
			
			List<HtmlLink> toBePrinted = new ArrayList<HtmlLink>();
			
			while(it.hasNext())
			{
				HtmlLink item = (HtmlLink)it.next();
				if(item.getLink()!=null && item.getLink().contains(domain) && !linkCrawled.contains(item.getLink()))
				{

					linkCrawled.add(item.getLink());
					toBePrinted.add(item);
				}
			}
			
			Iterator it2 = toBePrinted.iterator();
			
			while(it2.hasNext())
			{
				HtmlLink item = (HtmlLink)it2.next();
				if(item.getLink()!=null && item.getLink().contains(domain))
				{
					for(int i=0;i<level;i++)
					{
						System.out.print(" ");
					}
					System.out.println(item.getLink());
					
					crawlURL(item.getLink(),level+1);
				}
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
	



