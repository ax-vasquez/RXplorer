package com.scriptient.rxplorer.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scriptient.rxplorer.R;
import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;

import java.util.List;
import java.util.Map;

public class LogEntryDetailParameterAdapter extends RecyclerView.Adapter<LogEntryDetailParameterAdapter.ViewHolder> {

    private List<LoggerBotEntryParameter> logEntryParameters;

    private Map<String, String> parameterMap;

    public LogEntryDetailParameterAdapter( Map<String, String> parameterMap ) {

        this.parameterMap = parameterMap;

    }

    public LogEntryDetailParameterAdapter( List<LoggerBotEntryParameter> logEntryParameters ) {

        this.logEntryParameters = logEntryParameters;

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
        holder.mTextViewParameterValue.setText( parameter.getParameterValue() );

    }

    @Override
    public int getItemCount() {
        return logEntryParameters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewParameterDataType;
        TextView mTextViewParameterValue;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextViewParameterDataType = itemView.findViewById( R.id.parameter_data_type );
            mTextViewParameterValue = itemView.findViewById( R.id.parameter_value );

        }
    }

}
