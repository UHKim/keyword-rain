package com.uhkim.acidnews;

import java.io.IOException;
import java.util.Vector;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;

import com.uhkim.acidnews.snow.TopSearchParser;
import com.uhkim.acidnews.snow.TopSearchSnow;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public class ProcessVisual extends PApplet{
	
	public final float accGround=(float) 9.8;
	Vector<TopSearchSnow> snowDeposit= new Vector<TopSearchSnow>(0);

	public void settings(){
		fullScreen();
    	fillSnow();

	}

	public void fillSnow(){
		TopSearchParser portalParser=new TopSearchParser(TopSearchParser.SiteName.naver);
    	snowDeposit.addAll(portalParser.genSnowList());
    	try {
			portalParser.getDocument(TopSearchParser.SiteName.daum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(snowDeposit.toString());

    	snowDeposit.addAll(portalParser.genSnowList());
	}
	class ScheduledJob extends TimerTask {
		   
		   public void run() {
		    	fillSnow();
		   }
		}
	
    public void setup(){
    	background(0,0,0);

    // Uncomment the following two lines to see the available fonts 
	  //String[] fontList = PFont.list();
	  //printArray(fontList);	  
    	ScheduledJob job = new ScheduledJob();
        Timer jobScheduler = new Timer();
        jobScheduler.scheduleAtFixedRate(job, 1000, 3000);
        try {
           Thread.sleep(500);
        } catch(InterruptedException ex) {
           //
        }
        jobScheduler.cancel();
    }
    
    public void mousePressed(){
    	float clickedX=mouseX;
    	float clickedY=mouseY;
		System.out.println("Clicked:"+clickedX+","+clickedY);
		
    	TopSearchSnow nearSnow=getNearestKeyword(clickedX,clickedY);
    	Runtime r = Runtime.getRuntime();
		System.out.println("Result:"+nearSnow.x+","+nearSnow.y);

		if (nearSnow!=null){
	    	try {
				r.exec("open -a safari "+nearSnow.newsURL);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Snow Not Found");
				e.printStackTrace();
			}
		}
    }
    
    public TopSearchSnow getNearestKeyword(float x, float y){
    	double min_distance=100000;
    	int min_index=9999;
    	for (int i=0; i<snowDeposit.size();i++){
    		TopSearchSnow snow=snowDeposit.get(i);
    		double distance=sqrt(pow(2,x-snow.x)+pow(2,y-snow.y));
    		if (distance<=min_distance){
    			min_index=i;
    			System.out.println("Min Updated to "+snow.searchWord);
    		}
    	}
    	if (min_index!=9999){
    		return snowDeposit.get(min_index);
    	}
    	return null;
    }
    
    public void draw(){
    	background(255,255,255);
    	int m = floor(millis()/1000);
    	

    	for (int i=0; i<snowDeposit.size();i++){
    		TopSearchSnow snow=snowDeposit.get(i);
			float scaleFactor=(float)1.0-((float)(i)/(float)snowDeposit.size());

    		if(snow.released==false){
    			snow.x=random(width);
    			snow.y=random(height);
    			
    			snow.alpha=random(150+floor(105*scaleFactor));
    			
    			snow.released=true;
    		}
    		else if (snow.released==true){
    			if (snow.alpha>5 && snow.y < height-5){
    				snow.alpha=snow.alpha-2*(1-scaleFactor);
    				snow.y=snow.y+1+(1-scaleFactor);
    			}
    			else{
        			snow.released=false;
    			}
    			textSize(32);
    			PFont myFont = createFont("AppleSDGothicNeo-Bold", 20+25*scaleFactor);
    			textFont(myFont);

    			text(snow.searchWord, snow.x, snow.y); 
    			if (snow.source==TopSearchParser.SiteName.daum){
    				fill(97,144,249, snow.alpha);
    			}
    			else if (snow.source==TopSearchParser.SiteName.naver){
    				fill(53, 178, 30, snow.alpha);
    			}
    			
    			
    		}else{
    			snow.released=false;
    		}
    	}
    }
    
    
}
