package com.scriptient.rxplorer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.scriptient.rxplorer.async.ModifyDatabaseAsyncTask;
import com.scriptient.rxplorer.persistence.model.log.AppEmbeddedLogEntry;

import java.util.List;

public class LoggerViewAdapter extends RecyclerView.Adapter<LoggerViewAdapter.ViewHolder> {

    public static final int DATA_SET_START = 0;

    private ModifyDatabaseAsyncTask modifyAsyncTask;

    private List<AppEmbeddedLogEntry> currentLogData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTimestampTextView;
        TextView mLogLevelTextView;
        TextView mParentMethodTextView;
        CheckBox mCheckBox;
        public ViewHolder(View v) {
            super(v);
            mTimestampTextView = v.findViewById( R.id.timestamp_label );
            mLogLevelTextView = v.findViewById( R.id.log_level_label );
            mParentMethodTextView = v.findViewById( R.id.method_call_label );
            mCheckBox = v.findViewById( R.id.checkbox_log_entry_is_saved );

        }
    }

    public LoggerViewAdapter( List<AppEmbeddedLogEntry> entriesToDisplay ) {

        currentLogData = entriesToDisplay;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_list_item_standard, parent, false);

        return new ViewHolder(v);
    }

    /**
     * Called when binding a recycled (or new) ViewHolder with new data
     *
     * @param holder                The holder being bound
     * @param position              The position of the holder within the Adapter
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AppEmbeddedLogEntry entry = currentLogData.get( position );

        // If the entry is saved
        if ( entry.getSaved() ) {

            // And the checkbox ISN'T checked
            if ( !holder.mCheckBox.isChecked() ) {

                holder.mCheckBox.toggle();

            }

        // If the entry isn't saved
        } else {

            // And the checkbox IS checked
            if ( holder.mCheckBox.isChecked() ) {

                holder.mCheckBox.toggle();

            }

        }

        CheckboxClickListener checkboxClickListener = new CheckboxClickListener( entry );
        holder.mCheckBox.setOnClickListener( checkboxClickListener );

        holder.mTimestampTextView.setText( entry.getTimestamp() );
        holder.mLogLevelTextView.setText( entry.getLogLevel() );
        holder.mParentMethodTextView.setText( entry.getParentMethod() );

        switch ( entry.getLogLevel() ) {

            case LoggerViewFragment.LOG_LEVEL_INFO:
                int infoTextColorId = holder.itemView.getResources().getColor( R.color.infoLogText );
                // Set Item Text Colors
                holder.mLogLevelTextView.setTextColor( infoTextColorId );
                holder.mParentMethodTextView.setTextColor( infoTextColorId );
                break;
            case LoggerViewFragment.LOG_LEVEL_WARN:
                int warnTextColorId = holder.itemView.getResources().getColor( R.color.warnLogText );
                // Set Item Text Colors
                holder.mLogLevelTextView.setTextColor(warnTextColorId);
                holder.mParentMethodTextView.setTextColor(warnTextColorId);
                break;
            case LoggerViewFragment.LOG_LEVEL_ERROR:
                int errorTextColorId = holder.itemView.getResources().getColor( R.color.errorLogText );
                // Set Item Text Colors
                holder.mLogLevelTextView.setTextColor( errorTextColorId );
                holder.mParentMethodTextView.setTextColor( errorTextColorId );
                break;
            default:
                // Leave formatting as-is when log level is verbose
                // Set Item Text Colors
                int colorId = holder.itemView.getResources().getColor( R.color.standardLogText );
                holder.mLogLevelTextView.setTextColor( colorId );
                holder.mParentMethodTextView.setTextColor( colorId );
                break;

        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return currentLogData.size();
    }

    public List<AppEmbeddedLogEntry> getCurrentLogData() {
        return currentLogData;
    }

    public void setCurrentLogData(List<AppEmbeddedLogEntry> currentLogData) {
        this.currentLogData = currentLogData;
    }

    public class CheckboxClickListener implements View.OnClickListener {

        AppEmbeddedLogEntry embeddedLogEntry;

        public CheckboxClickListener( AppEmbeddedLogEntry entry ) {

            this.embeddedLogEntry = entry;

        }

        @Override
        public void onClick(View view) {

            if ( embeddedLogEntry.getSaved() ) {

                embeddedLogEntry.setSaved( false );

            } else {

                embeddedLogEntry.setSaved( true );

            }

            // Commit changes to the app database
            modifyAsyncTask = new ModifyDatabaseAsyncTask( view, ModifyDatabaseAsyncTask.MODIFY_UPDATE, embeddedLogEntry );
            modifyAsyncTask.execute();
            notifyItemInserted( getItemCount() );

        }

    }
}
