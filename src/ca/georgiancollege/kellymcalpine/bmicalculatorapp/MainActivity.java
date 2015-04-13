/**
 * PROGRAM NAME:  COMP1011-Assignment05
 * PROGRAM DESCRIPTION:  Android Application - BMI Calculator 
 * @author Kelly McAlpine 200260425
 * @version 0.4 - Implemented methods for switch, buttons and BMI calculations				
 * Date last modified:  2015-04-12
 */

package ca.georgiancollege.kellymcalpine.bmicalculatorapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.DragEvent;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.util.AndroidException;


public class MainActivity extends Activity 
{
	//INSTANCE VARIABLE (FIELDS)**************************************************************************
	//Constants used when saving/restoring state
	private static final String WEIGHT_INPUT = "WEIGHT_INPUT";
	private static final String HEIGHT_INPUT = "HEIGHT_INPUT";
	private static final String BMI_RESULT = "BMI_RESULT";
	private static final String BMI_SCALE = "BMI_SCALE";
	
	private double _userHeight; //height entered by user
	private double _userWeight; //weight entered by user
	private double _heightInInches;
	private double heightInMetres;
	private double _weightInPounds;
	private double _weightInKilograms;
	private double _bmiImperialTotal;
	private double _bmiMetricTotal;
	private String _bmiScale;
	private double _bmiCalculation; //BMI calculation based on user height & weight inputs
	private TextView _imperialTextView; //to display imperial measurements
	private TextView _metricTextView; //to display metric measurements
	private EditText _heightEditText; //displays user's height input
	private EditText _weightEditText; //displays user's weight input
	private EditText _resultEditText; //displays BMI calculation
	private EditText _bmiScaleEditText; //to display the BMI scale category (that calculation result falls into)
	private Switch _unitSwitch; //toggle between user's choice of imperial or metric
	private TextView _unitSwitchStatusTextView;
	private Button _calculateButton; //calculates the BMI based on the user's height and weight inputs
	private Button _resetButton;
	private TextWatcher	_heightEditTextWatcher;
	private TextWatcher	_weightEditTextWatcher;
	
    //PROTECTED OVERRIDE METHODS********************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  // call Superclass' version
        setContentView(R.layout.activity_main); //inflate the GUI
        
        //Check if app just started or is restoring from memory
        if (savedInstanceState == null)
        {
        	this._userHeight = 0; // initialize the height amount as zero
        	this._userWeight = 0; //initialize the weight amount as zero
        	this._bmiCalculation = 0; //initialize the BMI calculation as zero
        } //end if
        else //app is being restored from memory, not executed from scratch
        {
        	//initialize the height, weight, BMI calculation & scale to saved amount
        	this._userHeight = savedInstanceState.getDouble(HEIGHT_INPUT);
        	this._userWeight = savedInstanceState.getDouble(WEIGHT_INPUT);
        	this._bmiCalculation = savedInstanceState.getDouble(BMI_RESULT);
        	//this.bmiScaleEditText = savedInstanceState.getString(BMI_SCALE);
        } //end else
        
        //get references to the height, weight, BMI and scale editTexts
        this._heightEditText = (EditText) findViewById(R.id.heightEditText);
        this._weightEditText = (EditText) findViewById(R.id.weightEditText);
        this._resultEditText = (EditText) findViewById(R.id.resultEditText);
        this._bmiScaleEditText = (EditText) findViewById(R.id.BMIScaleEditText);
        
        //get the TextView displaying the Imperial & metric measurements
        this._imperialTextView = (TextView) findViewById(R.id.imperialTextView);
        this._metricTextView = (TextView) findViewById(R.id.metricTextView);
        
        // EditTextWatcher handles height & weight EditTexts' onTextChanged event
        this._heightEditText.addTextChangedListener(this._heightEditTextWatcher);
        this._weightEditText.addTextChangedListener(this._weightEditTextWatcher);
        
        //Used sample code for switch from http://www.mysamplecode.com/2013/04/android-switch-button-example.html
        //get the switch used to choose between measurement types
        final Switch unitSwitch = (Switch) findViewById(R.id.unitSwitch);
        this._unitSwitchStatusTextView = (TextView) findViewById(R.id.unitSwitchStatusTextView);
        //set the switch to ON for Metric measurements 
        //(OFF is the default state of the app which shows imperial measurements unless switched by the user)
        this._unitSwitch.setChecked(true);
        
        //attach a listener to check for changes in state
        unitSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
			private double	userHeight;
			private Object	heightEditText;
			private double	userWeight;
			private Object	weightEditText;
			private Object	resultEditText;

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
        		if (unitSwitch.isChecked())
        		{
        			_unitSwitchStatusTextView.setText("METRIC");
        	    	//convert the user input into metric measurements
        	    	heightInMetres = this.userHeight * 0.0254;
        	    	((TextView) this.heightEditText).setText(String.format("%.02f", heightInMetres));
        	    	_weightInKilograms = this.userWeight * 0.453592;
        	    	((TextView) this.weightEditText).setText(String.format("%.02f", _weightInKilograms));
        	    	

        		}
        		else
        		{
        			_unitSwitchStatusTextView.setText("IMPERIAL");
        	    	//Set the default measurements to imperial
        	    	_heightInInches = this.userHeight;
        	    	((TextView) this.heightEditText).setText(String.format("%.02f", _heightInInches));
        	    	_weightInPounds = this.userWeight;
        	    	((TextView) this.weightEditText).setText(String.format("%.02f", _weightInPounds));
        	    	
        		}
				
			}
			
			//PUBLIC METHODS***********************************************************************************
			public void onStartTrackingTouch(Switch unitSwitch){}
			
			public void onStopTrackingTouch(Switch unitSwitch) {}
        });
        
               
        //get the buttons used to calculate the BMI and reset app
        Button calculateButton = (Button) findViewById(R.id.calculateButton);
        Button resetButton = (Button) findViewById(R.id.resetButton);
        
        //http://developer.android.com/guide/topics/ui/controls/button.html
        //calculateButton response method
        this._calculateButton.setOnClickListener(new View.OnClickListener()
		{
			
			private TextView	resultEditText;

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(unitSwitch.isChecked())
				{
        	    	//Metric BMI Calculation
        	    	_bmiMetricTotal = _weightInKilograms / (heightInMetres * heightInMetres);
        	    	((TextView) this.resultEditText).setText(String.format("%.02f", _bmiMetricTotal));
				}
				else
				{
        	    	//Imperial BMI Calculation
        	    	_bmiImperialTotal = (_weightInPounds * 703) / (_heightInInches * _heightInInches);
        	    	((TextView) this.resultEditText).setText(String.format("%.02f", _bmiImperialTotal));
				}
			}
		});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) 
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
} //End of class MainActivity
