import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class osuMapTool {
	private static ArrayList<String> _map = new ArrayList<String>(); //Holds name of map
	private static ArrayList<String> _mapper = new ArrayList<String>(); //Holds name of mapper/map
	private static ArrayList<String> _plays = new ArrayList<String>(); //Holds number of plays/map
	private static ArrayList<String> _id = new ArrayList<String>(); //Holds the URL id of map
	private static ArrayList<String> _profile = new ArrayList<String>(); //Holds the URL ids of mapper profiles
	private static ArrayList<String> _reccMaps = new ArrayList<String>();
	private static String[] prefix = {"Stars: ", "Length: ", "BPM: ", "Success rate: "}; //Prefixes used when printing out info
	private static Document doc; 
	
	/*Sets doc to retrieved data from site*/
	public static void fetchSite() throws IOException{
		doc = Jsoup.connect("https://osu.ppy.sh/").get();
	}
	/*Adds map to _map. Necessary info is found in the <a> elements within div id most_played*/
	public static void addMap() throws Exception{
		fetchSite();
		Elements row1 = doc.select("div#most_played tr.row1 td a");
		Elements row2 = doc.select("div#most_played tr.row2 td a");
		for(int i = 0; i < row1.size()-1; i+=2){
			String map = row1.get(i).text();
			_map.add(map);	
		}
		for(int i =0; i < row2.size()-1; i+=2){
			String map = row2.get(i).text();
			_map.add(map);
		}
	}
	/*Like addMap() but for mappers*/
	public static void addMapper() throws Exception{
		fetchSite();
		Elements row1 = doc.select("div#most_played tr.row1 td a");
		Elements row2 = doc.select("div#most_played tr.row2 td a");
		for(int i = 0; i < row1.size()-1; i+=2){
			String mapper = row1.get(i+1).text();
			_mapper.add(mapper);
		}
		for(int i = 0; i < row2.size()-1; i+=2){
			String mapper = row2.get(i+1).text();
			_mapper.add(mapper);
		}
	}
	/*Like addMap() but for plays*/
	public static void addPlays() throws Exception{
		fetchSite();
		Elements row1 = doc.select("div#most_played tr.row1 td");
		Elements row2 = doc.select("div#most_played tr.row2 td");
		for(int i = 0; i < row1.size()-2; i+=3){
			String plays = row1.get(i).text();
			_plays.add(plays);
		}
		for(int i = 0; i < row2.size()-2; i+=3){
			String plays = row2.get(i).text();
			_plays.add(plays);
		}
	}
	/*Like addMap() but for map URL ids*/
	public static void addId() throws Exception{
		fetchSite();
		Elements row1 = doc.select("div#most_played tr.row1 td a");
		Elements row2 = doc.select("div#most_played tr.row2 td a");
		for(int i = 0; i < row1.size()-1; i+=2){
			String id = row1.get(i).attr("abs:href");
			_id.add(id);
		}
		for(int i = 0; i < row2.size()-1; i+=2){
			String id = row2.get(i).attr("abs:href");
			_id.add(id);
		}
	}
	/*Like addMap() but for mapper profile URL ids*/
	public static void addProfile() throws Exception{
		fetchSite();
		Elements row1 = doc.select("div#most_played tr.row1 td a");
		Elements row2 = doc.select("div#most_played tr.row2 td a");
		for(int i = 1; i < row1.size(); i+=2){
			String id = row1.get(i).attr("abs:href");
			_profile.add(id);
		}
		for(int i = 1; i < row2.size(); i+=2){
			String id = row2.get(i).attr("abs:href");
			_profile.add(id);
		}
	}
	/*Reorder elements within ArrayLists so they are printed in order from most played to least*/
	public static void reOrder(ArrayList<String> data){
		String temp = data.get(3);
		String temp2 = data.get(1);
		data.set(3, data.get(2));
		data.set(1, temp);
		data.set(2, temp2);
		String temp3 = data.get(3);
		data.set(3,data.get(4));
		data.set(4,temp3);
	}
	/*Prints out additional information about selected map gotten using its URL id*/
	public static void getMapInfo(int map) throws Exception{
		Document maps = Jsoup.connect(_id.get(map)).get();
		Elements info = maps.select("table#songinfo tr td.colour");
		int prefix_index = 0;
		for(int i = 5; i < info.size()-2; i+=3){
			System.out.println(prefix[prefix_index] + info.get(i).text());
			prefix_index++;
		}			
	}
	/*Obtains additional information about selected mapper with Selenium. Adds other maps done by mapper to _reccMaps*/
	public static void getMapperInfo(int mapper) throws Exception{
		System.setProperty("webdriver.gecko.driver", "C:\\Users\\Thomas\\Desktop\\geckodriver\\geckodriver.exe");
		WebDriver browser = new FirefoxDriver();
		browser.get(_profile.get(mapper));
		browser.findElement(By.id("_beatmaps")).click();
		Thread.sleep(5000);
		browser.findElement(By.xpath("//*[@id='beatmaps']/div[3]")).click();
		Thread.sleep(5000);
		WebElement findElement = browser.findElement(By.xpath("//*[@id='beatmapsRanked']"));
		String maps = findElement.getText();
		_reccMaps.set(mapper, maps);
		browser.close();
		displayMapperInfo(mapper);
	}
	public static void displayMapperInfo(int mapper){
		System.out.println(_reccMaps.get(mapper));
	}
	/*Takes user input to find out more information about certain map or mapper*/
	@SuppressWarnings("resource")
	public static void userInput() throws Exception{
		Scanner scan = new Scanner(System.in);
		String s = "";
		Boolean run = true;
		System.out.println("Enter: ");
		while(run == true){
			s = scan.next() + " " + scan.next();
			if(s.equals("!info 1")){
				System.out.println("Additonal info for " + _map.get(0));
				getMapInfo(0);
			}
			else if(s.equals("!info 2")){
				System.out.println("Additonal info for " + _map.get(1));
				getMapInfo(1);
			}
			else if(s.equals("!info 3")){
				System.out.println("Additonal info for " + _map.get(2));
				getMapInfo(2);
			}
			else if(s.equals("!info 4")){
				System.out.println("Additonal info for " + _map.get(3));
				getMapInfo(3);
			}
			else if(s.equals("!info 5")){
				System.out.println("Additonal info for " + _map.get(4));
				getMapInfo(4);
			}
			else if(s.equals("!mapper 1")){
				System.out.println("Additonal info for " + _mapper.get(0));
				getMapperInfo(0);
			}
			else if(s.equals("!mapper 2")){
				System.out.println("Additonal info for " + _mapper.get(1));
				getMapperInfo(1);
			}
			else if(s.equals("!mapper 3")){
				System.out.println("Additonal info for " + _mapper.get(2));
				getMapperInfo(2);
			}
			else if(s.equals("!mapper 4")){
				System.out.println("Additonal info for " + _mapper.get(3));
				getMapperInfo(3);
			}
			else if(s.equals("!mapper 5")){
				System.out.println("Additonal info for " + _mapper.get(4));
				getMapperInfo(4);
			}
			else{
				System.out.println("Map/Mapper not found");
			}
		}
	}
	/*Formats info so it actually makes sense when printed*/
	public static void format(){
		for(int i = 0; i < _map.size(); i++){
			System.out.println( (i+1) + ". " + _map.get(i)+ " mapped by " +_mapper.get(i)+ "  |  Played " +_plays.get(i) + " times");
		}
	}

	public static void fillArr(ArrayList<String> data){
		for(int i = 0; i < 10; i++){
			data.add(i+"");
		}
	}
	
	public static void main(String[] args) throws Exception{
		fillArr(_reccMaps);
		addMap();
		addMapper();
		addPlays();
		addId();
		addProfile();
		reOrder(_map);
		reOrder(_mapper);
		reOrder(_plays);
		reOrder(_id);
		reOrder(_profile);
		System.out.println(_profile);
		format();
		userInput();
	}
}
