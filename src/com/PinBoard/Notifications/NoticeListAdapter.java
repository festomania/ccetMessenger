package com.PinBoard.Notifications;
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;



import org.Ccet.Messenger.Pingbook.R;

import com.parse.ParseFile;
import com.parse.ParseObject;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoticeListAdapter extends BaseAdapter{
	
	private List<ParseObject> listOfNoticesObj;
	private LayoutInflater mInflater;
	Context context;
	String heading;
	String content;
	
	public NoticeListAdapter(Context context,List<ParseObject> listofobjects, String heading, String content) {
		this.listOfNoticesObj=listofobjects;
		this.context=context;
		this.heading=heading;
		this.content=content;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listOfNoticesObj.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup parent) {
		View row=convertView;
		ViewHolder holder=null;
		if (row == null) {
			mInflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			row = mInflater.inflate(R.layout.notice_list_child,parent, false);
			holder = new ViewHolder(row);
			row.setTag(holder);
		}
		else{
			holder=(ViewHolder) row.getTag();
		}

		holder.heading.setText((String)listOfNoticesObj.get(arg0).get(heading));
		holder.content.setText((String)listOfNoticesObj.get(arg0).get(content));
		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		String s = formatter.format(listOfNoticesObj.get(arg0).getUpdatedAt());
		holder.date.setText(s);
		formatter = new SimpleDateFormat("HH:mm:ss");
		s = formatter.format(listOfNoticesObj.get(arg0).getUpdatedAt());
		holder.time.setText("Time: "+s);
		
		
		
		row.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//ParseFile image= (ParseFile)listOfNoticesObj.get(arg0).get("ImageFile");
				Intent intent=new Intent(context, SingleNoticeDetails.class);
				intent.putExtra("heading", (String)listOfNoticesObj.get(arg0).get(heading));
				intent.putExtra("content", (String)listOfNoticesObj.get(arg0).get(content));
				try {
					ParseFile image= (ParseFile)listOfNoticesObj.get(arg0).get("ImageFile");
					Log.e("ImageUrl->",(String)image.getUrl());
					intent.putExtra("imageUrl", (String)image.getUrl());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("Image Error","NoticeAdapter-> "+e.getMessage());
					e.printStackTrace();
				}
				context.startActivity(intent);
			}
		});
		
		return row;	
	}
	
	class ViewHolder{
		TextView heading;
		TextView content;
		TextView date;
		TextView time;

		public ViewHolder(View convertView) {
			heading=(TextView) convertView.findViewById(R.id.heading);
			content=(TextView) convertView.findViewById(R.id.content);
			date=(TextView) convertView.findViewById(R.id.date);
			time=(TextView) convertView.findViewById(R.id.time);
		}
	}
	
}
