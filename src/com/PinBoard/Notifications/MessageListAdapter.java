package com.PinBoard.Notifications;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.Ccet.Messenger.Pingbook.R;
import com.parse.ParseObject;
import com.PinBoard.Notifications.NoticeListAdapter.ViewHolder;

public class MessageListAdapter extends BaseAdapter{
		
		private List<ParseObject> listOfNoticesObj;
		private LayoutInflater mInflater;
		Context context;
		
		public MessageListAdapter(Context context,List<ParseObject> listofobjects) {
			this.listOfNoticesObj=listofobjects;
			this.context=context;
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
				row = mInflater.inflate(R.layout.message_list_child,parent, false);
				holder = new ViewHolder(row);
				row.setTag(holder);
			}
			else{
				holder=(ViewHolder) row.getTag();
			}

			holder.heading.setText("By: "+(String)listOfNoticesObj.get(arg0).get("senderName"));
			holder.content.setText((String)listOfNoticesObj.get(arg0).get("message"));
			
			Format formatter = new SimpleDateFormat("yyyy-MM-dd");
			String s = formatter.format(listOfNoticesObj.get(arg0).getUpdatedAt());
			holder.date.setText(s);
			formatter = new SimpleDateFormat("HH:mm:ss");
			s = formatter.format(listOfNoticesObj.get(arg0).getUpdatedAt());
			holder.time.setText("Time: "+s);
			
			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					Intent intent=new Intent(context, SingleNoticeDetails.class);
					intent.putExtra("heading", "By: "+(String)listOfNoticesObj.get(arg0).get("senderName"));
					intent.putExtra("content", (String)listOfNoticesObj.get(arg0).get("message"));
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
				heading=(TextView) convertView.findViewById(R.id.msgheading);
				content=(TextView) convertView.findViewById(R.id.msgcontent);
				date=(TextView) convertView.findViewById(R.id.msgdate);
				time=(TextView) convertView.findViewById(R.id.msgtime);
			}
		}
		
	}
