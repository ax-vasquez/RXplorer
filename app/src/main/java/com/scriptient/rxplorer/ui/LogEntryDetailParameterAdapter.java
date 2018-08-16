package com.scriptient.rxplorer.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scriptient.rxplorer.R;
import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;

import java.util.List;

public class LogEntryDetailParameterAdapter extends RecyclerView.Adapter<LogEntryDetailParameterAdapter.ViewHolder> {

    private List<LoggerBotEntryParameter> logEntryParameters;

    public LogEntryDetailParameterAdapter( List<LoggerBotEntryParameter> logEntryParameters ) {

        this.logEntryParameters = logEntryParameters;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewParameterDataType;
        TextView mTextViewParameterName;
        TextView mTextViewParameterValue;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextViewParameterDataType = itemView.findViewById( R.id.parameter_data_type );
            mTextViewParameterName = itemView.findViewById( R.id.parameter_name );
            mTextViewParameterValue = itemView.findViewById( R.id.parameter_value );

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.log_entry_parameter_list_item, parent, false );

        return new ViewHolder( v );

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LoggerBotEntryParameter parameter = logEntryParameters.get( position );

        holder.mTextViewParameterDataType.setText( parameter.getParameterDataType() );
        holder.mTextViewParameterName.setText( parameter.getParameterName() );
        holder.mTextViewParameterValue.setText( parameter.getParameterValue() );

    }

    @Override
    public int getItemCount() {
        return logEntryParameters.size();
    }

}
