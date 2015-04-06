/**
 * PROGRAM NAME:  COMP1011-Assignment05
 * PROGRAM DESCRIPTION:  Android Application - BMI Calculator 
 * @author Kelly McAlpine 200260425
 * @version 0.3 - Initialization of all references 				
 * Date last modified:  2015-04-04
 */

package ca.georgiancollege.kellymcalpine.bmicalculatorapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.DragEvent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.util.AndroidException;


public class MainActivity extends Activity 
{
	//Constants used when saving/restoring state
	private static final String WEIGHT_INPUT = "WEIGHT_INPUT";
	private static final String HEIGHT_INPUT = "HEIGHT_INPUT";
	private static final String BMI_RESULT = "BMI_RESULT";
	private static final String BMI_SCALE = "BMI_SCALE";
	
	private double userHeight; //height entered by user
	private double userWeight; //weight entered by user
	private double bmiCalculation; //BMI calculation based on user height & weight inputs
	private TextView imperialTextView; //to display imperial measurements
	private TextView metricTextView; //to display metric measurements
	private EditText heightEditText; //displays user's height input
	private EditText weightEditText; //displays user's weight input
	private EditText resultEditText; //displays BMI calculation
	private EditText bmiScaleEditText; //to display the BMI scale category (that calculation result falls into)
	private Switch unitSwitch; //toggle between user's choice of imperial or metric
	private Button calculateButton; //calculates the BMI based on the user's height and weight inputs
	private Button resetButton;
	
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  // call Superclass' version
        setContentView(R.layout.activity_main); //inflate the GUI
        
        //Check if app just started or is restoring from memory
        if (savedInstanceState == null)
        {
        	this.userHeight = 0; // initialize the height amount as zero
        	this.userWeight = 0; //initialize the weight amount as zero
        	this.bmiCalculation = 0; //initialize the BMI calculation as zero
        } //end if
        else //app is being restored from memory, not executed from scratch
        {
        	//initialize the height, weight, BMI calculation & scale to saved amount
        	this.userHeight = savedInstanceState.getDouble(HEIGHT_INPUT);
        	this.userWeight = savedInstanceState.getDouble(WEIGHT_INPUT);
        	this.bmiCalculation = savedInstanceState.getDouble(BMI_RESULT);
        	//this.bmiScaleEditText = savedInstanceState.getString(BMI_SCALE);
        } //end else
        
        //get references to the height, weight, BMI and scale editTexts
        this.heightEditText = (EditText) findViewById(R.id.heightEditText);
        this.weightEditText = (EditText) findViewById(R.id.weightEditText);
        this.resultEditText = (EditText) findViewById(R.id.resultEditText);
        this.bmiScaleEditText = (EditText) findViewById(R.id.BMIScaleEditText);
        
        //get the TextView displaying the Imperial & metric measurements
        this.imperialTextView = (TextView) findViewById(R.id.imperialTextView);
        this.metricTextView = (TextView) findViewById(R.id.imperialTextView);
        
     // EditTextWatcher handles height & weight EditTexts' onTextChanged event
        //this.heightEditText.addTextChangedListener(this.heightEditTextWatcher);
        //this.weightEditText.addTextChangedListener(this.weightEditTextWatcher);
        
        //get the switch used to choose between measurement types
        Switch unitSwitch = (Switch) findViewById(R.id.unitSwitch);
        
        //get the buttons used to calculate the BMI and reset app
        Button calculateButton = (Button) findViewById(R.id.calculateButton);
        Button resetButton = (Button) findViewById(R.id.resetButton);
        
        //this.unitSwitch.addOnLayoutChangeListener(this.unitSwitchListener);
        //this.calculateButton.addTextChangedListener(this.calculateButton);
    }
    
    //UTILITY METHODS**********************************************************
    private void _bmiCalculations() //update & set measurements, cacluate BMI
    {
    	double heightInInches;
    	double heightInMetres;
    	double weightInPounds;
    	double weightInKilograms;
    	double bmiImperialTotal;
    	double bmiMetricTotal;
    	String bmiScale;
    	
    	//Set the default measurements to imperial
    	heightInInches = this.userHeight;
    	this.heightEditText.setText(String.format("%.02f", heightInInches));
    	weightInPounds = this.userWeight;
    	this.weightEditText.setText(String.format("%.02f", weightInPounds));
    	
    	//convert the user input into metric measurements
    	heightInMetres = this.userHeight * 0.0254;
    	this.heightEditText.setText(String.format("%.02f", heightInMetres));
    	weightInKilograms = this.userWeight * 0.453592;
    	this.weightEditText.setText(String.format("%.02f", weightInKilograms));
    	
    	//Imperial BMI Calculation
    	bmiImperialTotal = (weightInPounds * 703) / (heightInInches * heightInInches);
    	this.resultEditText.setText(String.format("%.02f", bmiImperialTotal));
    	
    	
    	//Metric BMI Calculation
    	bmiMetricTotal = weightInKilograms / (heightInMetres * heightInMetres);
    	this.resultEditText.setText(String.format("%.02f", bmiMetricTotal));
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
}
