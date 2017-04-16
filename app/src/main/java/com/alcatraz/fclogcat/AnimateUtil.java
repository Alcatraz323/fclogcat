package com.alcatraz.fclogcat;

import android.animation.*;
import android.os.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

public class AnimateUtil
{
	public static void playstart(final View v){
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
			
			Animator a=ViewAnimationUtils.createCircularReveal(v,(int)v.getX(),(int)v.getY(),0,(float)Math.hypot((double)v.getWidth(),(double)v.getHeight()));
			a.setInterpolator(new AccelerateInterpolator());
			a.setDuration(750);
			a.start();
			a.addListener(new Animator.AnimatorListener(){

					@Override
					public void onAnimationStart(Animator p1)
					{
						// TODO: Implement this method
					}

					@Override
					public void onAnimationEnd(Animator p1)
					{
						v.setVisibility(View.VISIBLE);
						// TODO: Implement this method
					}

					@Override
					public void onAnimationCancel(Animator p1)
					{
						// TODO: Implement this method
					}

					@Override
					public void onAnimationRepeat(Animator p1)
					{
						// TODO: Implement this method
					}
				});
			AnimationSet as=new AnimationSet(true);
			AlphaAnimation aa=new AlphaAnimation(0,1);
			aa.setDuration(700);
			as.addAnimation(aa);
			v.startAnimation(as);
			as.setAnimationListener(new Animation.AnimationListener(){

					@Override
					public void onAnimationStart(Animation p1)
					{
						// TODO: Implement this method
					}

					@Override
					public void onAnimationEnd(Animation p1)
					{
						v.setVisibility(View.VISIBLE);
						// TODO: Implement this method
					}

					@Override
					public void onAnimationRepeat(Animation p1)
					{
						// TODO: Implement this method
					}
				});
		}else{
		
		AnimationSet as=new AnimationSet(true);
		AlphaAnimation aa=new AlphaAnimation(0,1);
		aa.setDuration(700);
		as.addAnimation(aa);
		v.startAnimation(as);
		as.setAnimationListener(new Animation.AnimationListener(){

				@Override
				public void onAnimationStart(Animation p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onAnimationEnd(Animation p1)
				{
					v.setVisibility(View.VISIBLE);
					// TODO: Implement this method
				}

				@Override
				public void onAnimationRepeat(Animation p1)
				{
					// TODO: Implement this method
				}
			});
			}
	}
	public static void playEnd(final View v){
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
			Animator a1=ViewAnimationUtils.createCircularReveal(v,(int)v.getX(),(int)v.getY(),(float)Math.hypot(v.getWidth(),v.getHeight()),0);
			a1.setInterpolator(new AccelerateInterpolator());
			a1.setDuration(700);
			a1.start();
			a1.addListener(new Animator.AnimatorListener(){

					@Override
					public void onAnimationStart(Animator p1)
					{
						// TODO: Implement this method
					}

					@Override
					public void onAnimationEnd(Animator p1)
					{
						v.setVisibility(View.GONE);
						
						// TODO: Implement this method
					}

					@Override
					public void onAnimationCancel(Animator p1)
					{
						// TODO: Implement this method
					}

					@Override
					public void onAnimationRepeat(Animator p1)
					{
						// TODO: Implement this method
					}
				});
		}else{
			AnimationSet as=new AnimationSet(true);
			AlphaAnimation aa=new AlphaAnimation(0,1);
			aa.setDuration(700);
			as.addAnimation(aa);
			v.startAnimation(as);
			as.setAnimationListener(new Animation.AnimationListener(){

					@Override
					public void onAnimationStart(Animation p1)
					{
						// TODO: Implement this method
					}

					@Override
					public void onAnimationEnd(Animation p1)
					{
						v.setVisibility(View.GONE);
						// TODO: Implement this method
					}

					@Override
					public void onAnimationRepeat(Animation p1)
					{
						// TODO: Implement this method
					}
				});
		}
	}
	public static void textChange(final TextView txv,final String tx){
		AnimationSet as1=new AnimationSet(true);
		AlphaAnimation aa1=new AlphaAnimation(1,0);
		aa1.setDuration(200);
		as1.addAnimation(aa1);
		txv.startAnimation(as1);
		as1.setAnimationListener(new Animation.AnimationListener(){

				@Override
				public void onAnimationStart(Animation p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onAnimationEnd(Animation p1)
				{
					txv.setVisibility(View.GONE);
					txv.setText(tx);
					AnimationSet as=new AnimationSet(true);
					AlphaAnimation aa=new AlphaAnimation(0,1);
					aa.setDuration(200);
					as.addAnimation(aa);
					txv.startAnimation(as);
					as.setAnimationListener(new Animation.AnimationListener(){

							@Override
							public void onAnimationStart(Animation p1)
							{
								// TODO: Implement this method
							}

							@Override
							public void onAnimationEnd(Animation p1)
							{
								txv.setVisibility(View.VISIBLE);
								// TODO: Implement this method
							}

							@Override
							public void onAnimationRepeat(Animation p1)
							{
								// TODO: Implement this method
							}
						});
					// TODO: Implement this method
				}

				@Override
				public void onAnimationRepeat(Animation p1)
				{
					// TODO: Implement this method
				}
			});
		
	}
}
