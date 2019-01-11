package swap.irfanullah.com.swap;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

import swap.irfanullah.com.swap.Adapters.InvitePhoneContactAdapter;
import swap.irfanullah.com.swap.Models.PhoneContact;
import swap.irfanullah.com.swap.Models.RMsg;

public class InvitePhoneContactsActivity extends AppCompatActivity {

    private RecyclerView contactRV;
    private InvitePhoneContactAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<PhoneContact> contacts;
    private Context context;
    private SmsManager smsManager;
    private ContentResolver contentResolver;
    private Cursor contactsCursor;
    private int INVITES = 0;
    private int TOTAL_INVITES = 5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        getSupportActionBar().setTitle("Invite atleast 5 of your contacts");
        getSupportActionBar().setSubtitle(Integer.toString(TOTAL_INVITES - INVITES)+" remaining");
        smsManager = SmsManager.getDefault();
        contentResolver = getContentResolver();
        setContentView(R.layout.activity_invite_phone_contacts);
        contactRV = findViewById(R.id.contactPhoneNumber);

        layoutManager = new LinearLayoutManager(context);
        contactRV.setLayoutManager(layoutManager);
        contacts = new ArrayList<>();
        adapter = new InvitePhoneContactAdapter(context,contacts);

        adapter.setOnInviteClickListener(new InvitePhoneContactAdapter.InviteListener() {
            @Override
            public void onInvite(View v, int position) {
                Button invite = v.findViewById(R.id.inviteBtn);
                try{
                    smsManager.sendTextMessage(contacts.get(position).getCONTACT_NUMBER(),null,"I am going to invite you",null,null);
                    invite.setText("Invited");
                    INVITES++;
                    updateInviteCount();
                }catch (Exception e){
                    Snackbar.make(getCurrentFocus(),e.getMessage(),Snackbar.LENGTH_LONG).show();
                }

            }
        });

        contactRV.setAdapter(adapter);
        fetchContacts();


    }

    private void fetchContacts(){
        contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        if(contactsCursor.getCount() > 0){
            int i = 0;
            while (contactsCursor.moveToNext()){
                String id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if(hasPhoneNumber >0) {
                    Cursor phoneNumberCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    phoneNumberCursor.moveToFirst();

                    String number = phoneNumberCursor.getString(phoneNumberCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contacts.add(new PhoneContact(name, number));
                    phoneNumberCursor.close();
                }


            }

            RMsg.ilogHere(contacts.size());
            adapter.notifyAdapter(contacts);
            contactsCursor.close();
        }
    }

    private void updateInviteCount(){
        if(INVITES >=0 ) {
            getSupportActionBar().setSubtitle(Integer.toString(TOTAL_INVITES - INVITES) + " remaining");
        }else if(INVITES < 0) {
            getSupportActionBar().setSubtitle(Integer.toString(TOTAL_INVITES - INVITES) + " remaining");
        }



        if(INVITES >= 5){
            Snackbar.make(getCurrentFocus(),"Limit reached.",Snackbar.LENGTH_LONG).show();
        }
    }
}
