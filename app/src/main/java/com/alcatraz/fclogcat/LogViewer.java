package com.alcatraz.fclogcat;
import android.support.v7.app.*;
import android.os.*;
import android.support.v7.widget.*;
import android.widget.*;
import java.util.*;
import android.widget.AbsListView.*;
import android.view.*;
import android.content.*;
import android.graphics.drawable.*;
import com.alcatraz.support.v4.appcompat.*;
import android.graphics.*;
import java.io.*;
import android.content.pm.*;
import android.annotation.*;
import android.app.*;
import android.support.design.widget.*;
import android.util.*;

public class LogViewer extends AppCompatActivity
{
	android.support.v7.widget.Toolbar tb;
	NoScrollListView lv;
	TextView txv;
	TextView txv2;
	TextView txv3;
	LinearLayout ll;
	ImageView imgv;
	String exc;
	String cls="";
	ListViewAdapter lva;
	AppBarLayout abl;
	FloatingActionButton fab;
	CollapsingToolbarLayout ctl;
	Map<Integer,String> selected=new HashMap<Integer,String>();
	List<String> data=new ArrayList<String>();
	String pkg;
	String label;
	String path;
	Drawable icon;
	/*shared*/
	Set<String> set;
	String hb;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewer);
		initSetting();
		getData(getIntent());
		initData();
		initViews();
	}
	public void initSetting(){
		SharedPreferences spf=getSharedPreferences(getPackageName()+"_preferences",MODE_PRIVATE);
		set=spf.getStringSet("hldb",SpfConstants.getDefSet());
		hb=spf.getString("hb","#9fa8da");
	}
	@TargetApi(Build.VERSION_CODES.KITKAT)
    private void setTranslucentStatus(boolean on,Activity act) {
        Window win = act.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
	protected void Immersive(android.support.v7.widget.Toolbar mToolbar,boolean immersive,Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
		int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
		int paddingTop = mToolbar.getPaddingTop();
		int paddingBottom = mToolbar.getPaddingBottom();
		int paddingLeft = mToolbar.getPaddingLeft();
		int paddingRight = mToolbar.getPaddingRight();
		ll.setPadding(ll.getPaddingLeft(),statusBarHeight,ll.getPaddingRight(),ll.getPaddingBottom());
		ViewGroup.LayoutParams params = mToolbar.getLayoutParams();
		int height = params.height;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true,activity);
			if (immersive) {
				paddingTop += statusBarHeight;
				height += statusBarHeight;
			} else {
				paddingTop -= statusBarHeight;
				height -= statusBarHeight;
			}

            params.height = height;
            mToolbar.setPadding(paddingLeft,paddingTop, paddingRight, paddingBottom);

        }
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch(item.getItemId()){
			
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	public int dpToPx(Context context,int dp) {
	    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
	public void initViews()
	{
		tb=(android.support.v7.widget.Toolbar) findViewById(R.id.mytoolbar_1);
		setSupportActionBar(tb);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ll=(LinearLayout) findViewById(R.id.viewerLinearLayout1);
		Immersive(tb,true,this);
		abl=(AppBarLayout) findViewById(R.id.appbar);
		ctl=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
		ctl.setTitle(label);
		ctl.setCollapsedTitleTextColor(Color.WHITE);
		ctl.setExpandedTitleColor(Color.WHITE);
		txv=(TextView) findViewById(R.id.viewercontentTextView1);
		txv2=(TextView) findViewById(R.id.viewercontentTextView2);
		txv3=(TextView) findViewById(R.id.viewercontentTextView3);
		txv2.setText(txv2.getText().toString()+path);
		txv3.setText(txv3.getText().toString()+exc);
		txv.setText(txv.getText().toString()+cls);
		fab=(FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					android.support.v7.app.AlertDialog a=new android.support.v7.app.AlertDialog.Builder(LogViewer.this)
						.setTitle(R.string.delete_ad_title)
						.setMessage(R.string.delete_ad_msg)
						.setNegativeButton(R.string.delete_ad_nb,null)
						.setPositiveButton(R.string.delete_ad_pb,new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								File f=new File(path);
								f.delete();
								finish();
								// TODO: Implement this method
							}
						}).create();
					new AlertDialogUtil().setSupportDialogColor(a,Color.parseColor("#3f51b5"));
					a.show();
					// TODO: Implement this method
				}
			});
		lv=(NoScrollListView) findViewById(R.id.viewercontentListView1);
		lva=new ListViewAdapter(this,data,lv,pkg,set,hb);
		lv.setAdapter(lva);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		lv.setMultiChoiceModeListener(new MultiChoiceModeListener(){

				@Override
				public boolean onCreateActionMode(ActionMode p1, Menu p2)
				{
					// TODO: Implement this method

					p1.getMenuInflater().inflate(R.menu.action_mode,p2);
					return true;
				}

				@Override
				public boolean onPrepareActionMode(ActionMode p1, Menu p2)
				{
					// TODO: Implement this method
					return true;
				}
				public void copy(String content, Context context)  
				{  

					ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
					cmb.setText(content.trim());  
				}  
				@Override
				public boolean onActionItemClicked(ActionMode p1, MenuItem p2)
				{
					//ActionMode动作
					switch(p2.getItemId()){
						case R.id.act_item1:
							if(selected!=null){
								String t="";
								for(int i=0;i<data.size();i++){
									if(selected.containsKey(i)){
										t=t+selected.get(i).toString()+"\n";
									}
								}
								copy(t,LogViewer.this);
								selected.clear();
							}
							break;
						case R.id.act_item2:
							if(selected!=null){
								String t="";
								for(int i=0;i<data.size();i++){
									if(selected.containsKey(i)){
										t=t+selected.get(i).toString()+"\n";
									}
								}
								Intent i=new Intent(Intent.ACTION_SEND);
								i.setType("text/plain");
								i.putExtra(Intent.EXTRA_TEXT,t);
								startActivity(i);
								selected.clear();
							}
							break;
					}
					lv.clearChoices();
					p1.finish();
					// TODO: Implement this method
					return true;
				}
				@Override
				public void onDestroyActionMode(ActionMode p1)
				{
					selected.clear();
					// TODO: Implement this method
				}

				@Override
				public void onItemCheckedStateChanged(ActionMode p1, int p2, long p3, boolean p4)
				{
					if(p4){
						selected.put(p2,lv.getItemAtPosition(p2).toString());
					}else{
						selected.remove(p2);
					}
					p1.setTitle(getString(R.string.multi_1)+selected.size()+getString(R.string.multi_2));
					lva.notifyDataSetChanged();
					// TODO: Implement this method
				}
			});
	}
	public void initData(){
		File f=new File(path);
		try{
			FileInputStream fis=new FileInputStream(f);
			if(fis!=null){
				InputStreamReader isr=new InputStreamReader(fis);
				BufferedReader reader=new BufferedReader(isr);
				String line;
				int i=0;
				boolean hasGotExc=false;
				while((line=reader.readLine())!=null){
					data.add(line);
					if(i>=2&&!hasGotExc){
						if(line.contains("Exception")){
							exc=LogCatAnalyser.getException(line);
							hasGotExc=true;
						}
					}
					if(i>=2){
							if(line.contains("at "+pkg)){
								String[] pro_1=line.split(" ");
								cls=pro_1[pro_1.length-1]+"\n";
							}
					}
					i++;
				}
				fis.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void getData(Intent i)
	{
		Bundle data=i.getExtras();
		label=data.getString("label");
		path=data.getString("path");
		pkg=getPkgManual(path);
		icon=getIcon(pkg);
	}

	
	public Drawable getIcon(String pkg){
		PackageManager pm=getPackageManager();
		try{
			ApplicationInfo ai=pm.getApplicationInfo(pkg,PackageManager.GET_META_DATA);
			return pm.getApplicationIcon(ai);
		}catch(PackageManager.NameNotFoundException e){
			return null;
		}
	}
	public String getPkgManual(String p){
		String[] pro=p.split(" ");
		String[] pkg_t=pro[2].split("\\.");
		String pkg="";
		int k=0;
		for(String n:pkg_t){
			if(n.equals(pkg_t[pkg_t.length-1])){
				break;
			}
			if(k!=0){
				pkg=pkg+"."+n;
			}else{
				pkg=pkg+n;
			}
			k++;
		}
		return pkg;
	}
}
