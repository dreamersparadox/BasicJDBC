import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TheCrawler 
{
	
	static Scanner s = new Scanner(System.in);
	static String seq = null;
	static ArrayList<String> ulinks = new ArrayList<String>();	
	static ArrayList<String> vlinks = new ArrayList<String>();
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException 
	{
		System.out.println("Enter what you want to do -");
		System.out.println(""
				+ "1. Get Links\n"
				+ "2. Get Images\n"
				+ "3. Get CSS\n"
				+ "4. Get Scripts\n"
				+ "5. Exit");
		
		int o = s.nextInt();
		
		switch(o)
		{
			case 1:
				getLinks();
				break;
			case 2:
				getImages();
				break;
			case 3:
				getsCSS();
				break;
			case 4:
				getScripts();
				break;
			case 5:
				return;
			default:
				System.out.println("Wrong Input!!!");
				break;
		}
		
		System.out.println("Process has been completed!!!");
	}

	@SuppressWarnings("unused")
	private static void getScripts() throws IOException 
	{
		System.out.println("1. From URL\n2. From URL list");
		int n = s.nextInt();
		
		switch(n)
		{
			case 1:
				
				System.out.println("Enter the URL: ");
				String url1 = s.next();
				System.out.println("Enter the filename to store the script links: ");
				String fname = s.next();
				fname = fname+".txt";
				@SuppressWarnings("unused") File file = new File(fname);
				
				scriptList(url1, fname);
				break;
				
			case 2:
				
				System.out.println("Enter the filename with URLs: ");
				String fname1 = s.next();
				
				BufferedReader reader = new BufferedReader(new FileReader(fname1+".txt"));
				System.out.println("Enter the filename to store the script links: ");
				String fname2 = s.next();
				fname2 = fname2+".txt";
				File file1 = new File(fname2);
				
				String line = reader.readLine();
				while (line != null) 
				{
					scriptList(line, fname2);
					line = reader.readLine();
				}
				reader.close();
				break;
				
			default:
				System.out.println("Wrong Input!!");
		}
	}

	private static void scriptList(String url1, String fname) throws IOException 
	{

		Document doc = Jsoup.connect(url1).get();
		Elements media = doc.select("[src]");
		
		for (Element src : media) 
        {
            if (src.tagName().equals("script"))
            {
            	 FileWriter fw = new FileWriter(fname,true);
            	 fw.write(src.attr("abs:src")+"\n");
            	 fw.close();
            }
        }
	}

	@SuppressWarnings({ "resource", "unused" })
	private static void getsCSS() throws IOException 
	{
		System.out.println("1. From URL\n2. From URL list");
		int n = s.nextInt();
		
		switch(n)
		{
			case 1:
				
				System.out.println("Enter the URL: ");
				String url1 = s.next();
				System.out.println("Enter the filename to store the stylesheet links: ");
				String fname = s.next();
				fname = fname+".txt";
				File file = new File(fname);
				
				ssList(url1, fname);
				break;
				
			case 2:
				
				System.out.println("Enter the filename with URLs: ");
				String fname1 = s.next();
				
				BufferedReader reader = new BufferedReader(new FileReader(fname1+".txt"));
				System.out.println("Enter the filename to store the stylesheet links: ");
				String fname2 = s.next();
				fname2 = fname2+".txt";
				File file1 = new File(fname2);
				
				String line = reader.readLine();
				while (line != null) 
				{
					ssList(line, fname2);
					line = reader.readLine();
				}
				reader.close();
				break;
				
			default:
				System.out.println("Wrong INput!!");
		}
	}

	private static void ssList(String url, String fname) throws IOException 
	{
		Document doc = Jsoup.connect(url).get();
		Elements imports = doc.select("link[href]");
		
		for (Element link : imports) 
        {
        	if(link.attr("rel").equalsIgnoreCase("stylesheet"))
        	{
        		FileWriter fw = new FileWriter(fname,true);
            	fw.write(link.attr("abs:href")+"\n");
            	fw.close();
        	}
        }
	}

	@SuppressWarnings({ "resource", "unused" })
	private static void getImages() throws IOException 
	{
		System.out.println("1. From URL\n2. From URL list");
		int n = s.nextInt();
		
		switch(n)
		{
			case 1:
				
				System.out.println("Enter the URL: ");
				String url1 = s.next();
				System.out.println("Enter the filename to store the image links: ");
				String fname = s.next();
				fname = fname+".txt";
				File file = new File(fname);
				imgList(url1, fname);
				break;
				
			case 2:
				
				System.out.println("Enter the filename with URLs: ");
				String fname1 = s.next();
				
				BufferedReader reader = new BufferedReader(new FileReader(fname1+".txt"));
				System.out.println("Enter the filename to store the image links: ");
				String fname2 = s.next();
				fname2 = fname2+".txt";
				File file1 = new File(fname2);
				
				String line = reader.readLine();
				while (line != null) 
				{
					imgList(line, fname2);
					line = reader.readLine();
				}
				reader.close();
				break;
				
			default:
				System.out.println("Wrong INput!!");
		}
	}

	private static void imgList(String url, String fname) throws IOException 
	{
		
		Document doc = Jsoup.connect(url).get();
		Elements media = doc.select("[src]");
		
		for (Element src : media) 
        {
            if (src.tagName().equals("img"))
            {
            	 FileWriter fw = new FileWriter(fname,true);
            	 fw.write(src.attr("abs:src")+"\n");
            	 fw.close();
            }
        }
	}

	@SuppressWarnings("resource")
	private static void getLinks() throws IOException 
	{
		System.out.println("Enter the domain:");
		String domain = s.next();
		
		System.out.println("Enter the filename: ");
		String fname = s.next();
		
		seq = domain;
		
		get(domain);
		
		for(int i = 0; i<ulinks.size(); i++)
			System.out.println(ulinks.get(i));
		
		for(int i = 0; i<ulinks.size(); i++)
        {
        	try 
        	{
        		get(ulinks.get(i));
        	}
        	
        	catch (Exception e)
        	{
        		continue;
        	}
        }
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fname+".txt")));

        for(int i = 0; i<vlinks.size(); i++)
        {
        	writer.write(vlinks.get(i));
        	writer.newLine();
        }
        writer.close();
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void get(String url) throws IOException 
	{
		Document doc = null;
		Elements links = null;
		
		vlinks.add(url);

		if(url.startsWith(seq))
			System.out.println("Fetching links from: "+url);
    	
		if(url.startsWith(seq))
		{
			doc = Jsoup.connect(url).get();
			links = doc.select("a[href]");
		}
        
        for (Element link : links) 
        {
        	if(link.attr("abs:href").startsWith(seq))
        		ulinks.add(link.attr("abs:href"));
        }
               
        for(int i = 0; i<ulinks.size(); i++)
        	ulinks.set(i, ulinks.get(i).replaceAll("/$", ""));
        
        //Removing duplicate links
        Set set = new LinkedHashSet<>(); 
        set.addAll(ulinks); 
        ulinks.clear(); 
        ulinks.addAll(set); 
        	
	}
}
