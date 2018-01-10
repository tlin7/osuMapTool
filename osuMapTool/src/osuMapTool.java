import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class osuMapTool {
	private static ArrayList<String> _map = new ArrayList<String>();
	private static ArrayList<String> _mapper = new ArrayList<String>();
	private static ArrayList<String> _plays = new ArrayList<String>();
	private static ArrayList<String> _id = new ArrayList<String>();
	private static Document doc;
	
	public static void fetchSite() throws IOException{
		doc = Jsoup.connect("https://osu.ppy.sh/").get();
	}
	
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
	
	public static void getMapInfo(int map) throws Exception{
		Document maps = Jsoup.connect(_id.get(map)).get();
		Elements info = maps.select("table#songinfo tr td.colour");
		for(int i = 5; i < info.size()-2; i+=3){
			System.out.println(info.get(i).text());
		}
			
	}
	
	@SuppressWarnings("resource")
	public static void userInput() throws Exception{
		Scanner scan = new Scanner(System.in);
		String s = "";
		Boolean run = true;
		System.out.println("Enter: ");
		while(run == true){
			s = scan.next() + " " + scan.next();
			if(s.equals("!info 1")){
				System.out.println("map 1 info");
				getMapInfo(0);
			}
			else if(s.equals("!info 2")){
				System.out.println("map 2 info");
				getMapInfo(1);
			}
			else if(s.equals("!info 3")){
				System.out.println("map 3 info");
				getMapInfo(2);
			}
			else if(s.equals("!info 4")){
				System.out.println("map 4 info");
				getMapInfo(3);
			}
			else if(s.equals("!info 5")){
				System.out.println("map 5 info");
				getMapInfo(4);
			}
			else if(s.equals("!mapper 1")){
				System.out.println("mapper 1 info");
			}
			else if(s.equals("!mapper 2")){
				System.out.println("mapper 2 info");
			}
			else if(s.equals("!mapper 3")){
				System.out.println("mapper 3 info");
			}
			else if(s.equals("!mapper 4")){
				System.out.println("mapper 4 info");
			}
			else if(s.equals("!mapper 5")){
				System.out.println("mapper 5 info");
			}
			else{
				System.out.println("Map/Mapper not found");
			}
		}
	}
	
	public static void format(){
		for(int i = 0; i < _map.size(); i++){
			System.out.println( (i+1) + ". " + _map.get(i)+ " mapped by " +_mapper.get(i)+ "  |  Played " +_plays.get(i) + " times");
		}
	}

	public static String printArr(ArrayList<String> data){
		String ret = "";
		for(String s: data){
			ret += s + " ";
		}
		return ret;
	}
	
	public static void main(String[] args) throws Exception{
		addMap();
		addMapper();
		addPlays();
		addId();
		reOrder(_map);
		reOrder(_mapper);
		reOrder(_plays);
		reOrder(_id);
		System.out.println(_id);
		format();
		userInput();
	}
}
