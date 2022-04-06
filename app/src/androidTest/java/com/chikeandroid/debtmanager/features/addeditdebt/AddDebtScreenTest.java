package com.chikeandroid.debtmanager.features.addeditdebt;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.widget.DatePicker;

import com.chikeandroid.debtmanager.R;
import com.chikeandroid.debtmanager.util.TimeUtil;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Chike on 3/15/2017.
 * Tests for the add debt screen
 */
@RunWith(AndroidJUnit4.class)
public class AddDebtScreenTest {

    private Calendar mCalendar;

   /* @Rule
    public ActivityTestRule<AddEditDebtActivity> activityRule =
            new ActivityTestRule<>(AddEditDebtActivity.class);*/

   /* @Before
    public void stubContactIntent() {
        final Intent resultData = new Intent();
        resultData.setData(Uri.parse("content://com.android.contacts/data/519"));

        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(allOf(
                hasData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
                hasAction(Intent.ACTION_PICK))
        ).respondWith(result);
    }

    @Test
    public void pickContact_viewIsSet() {
        //Check to make sure the Uri field is empty
        onView(withId(R.id.et_phone_number)).check(matches(withText("")));

        //Start contact picker
        onView(withId(R.id.ib_contacts)).perform(click());

        //Verify that Uri was set properly.
        onView(withId(R.id.et_phone_number)).check(matches(withText(PHONE_NUMBER)));

        Intent resultData = new Intent();
        String phoneNumber = "123-345-6789";
        resultData.putExtra("phone", phoneNumber);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        // Set up result stubbing when an intent sent to "contacts" is seen.
        intending(toPackage("com.android.contacts")).respondWith(result);

        // User action that results in "contacts" activity being launched.
        // Launching activity expects phoneNumber to be returned and displays it on the screen.
        onView(withId(R.id.ib_contacts)).perform(click());
    }*/

   @Before
   public void setUpCalendar() {
       mCalendar = Calendar.getInstance();
   }

   @Test
   @Ignore
   public void shouldBeAbleToPickADueDateFromDatePicker() {

       int year = 2017;
       int month = 11;
       int day = 15;

       onView(withId(R.id.btn_date_due)).perform(click());
       onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month + 1, day));
       onView(withId(android.R.id.button1)).perform(click());

       mCalendar.set(Calendar.YEAR, year);
       mCalendar.set(Calendar.MONTH, month);
       mCalendar.set(Calendar.DAY_OF_MONTH, day);

       String dateString = "Date due: " + TimeUtil.millis2String(mCalendar.getTimeInMillis());
       onView(withId(R.id.btn_date_due)).check(matches(withText(dateString)));
   }

   @Test
   @Ignore
   public void shouldBeAbleToPickCreatedDateFromDatePicker() {

       int year = 2017;
       int month = 11;
       int day = 15;

       onView(withId(R.id.btn_date_created)).perform(click());
       onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month + 1, day));
       onView(withId(android.R.id.button1)).perform(click());

       mCalendar.set(Calendar.YEAR, year);
       mCalendar.set(Calendar.MONTH, month);
       mCalendar.set(Calendar.DAY_OF_MONTH, day);

       String dateString = "Created on: " + TimeUtil.millis2String(mCalendar.getTimeInMillis());
       onView(withId(R.id.btn_date_created)).check(matches(withText(dateString)));
   }

   @Test
   @Ignore
   public void shouldNotSaveEmptyDebt() {

       onView(withId(R.id.et_full_name)).perform(clearText());
       onView(withId(R.id.et_phone_number)).perform(clearText());
       onView(withId(R.id.et_amount)).perform(clearText());
       onView(withId(R.id.action_save_debt)).perform(click());

       onView(withId(R.id.et_comment)).check(matches(isDisplayed()));
   }

}
