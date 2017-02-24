package com.uhkim.acidnews.snow;

import processing.core.*;

public class TopSearchSnow {
	
	
	
	public float x;
	public float y;
	public float alpha;
	
	public int wordRank;
	
	public Boolean released;
	
	public String newsURL;
	public String searchWord;
	
	public float aeroFric;
	
	public TopSearchParser.SiteName source;
	
	public TopSearchSnow(String word){
		released=false;
		searchWord=word;
		alpha=1;
	}
	
	public TopSearchSnow(){
		released=false;
		searchWord="";
		alpha=1;
	}
	
	public void genRandCoord(int width, int height){
		
	}
}
