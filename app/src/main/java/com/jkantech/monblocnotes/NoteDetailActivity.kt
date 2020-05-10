package com.jkantech.monblocnotes

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.jkantech.monblocnotes.utils.deleteNote
import kotlinx.android.synthetic.main.add_notes.*

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        val REQUEST_EDIT_NOTE = 1

        val EXTRA_NOTE = "note"
        val EXTRA_NOTE_INDEX = "noteIndex"

        val ACTION_SAVE = "com.jkantech.monblocnotes.actions.ACTION_SAVE"
        val ACTION_DELETE = "com.jkantech.monblocnotes.action.ACTION_DELETE"
        lateinit var menu: Menu


    }

    lateinit var note: Note
    lateinit var editnote: Note
    var noteIndex: Int = -1
    var editnoteIndex: Int = -1
    lateinit var titleView:TextView
    lateinit var textView: TextView
    lateinit var EdittitleView:TextView
    lateinit var EdittextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)
        toolbar.setTitle("Detail")

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        note = intent.getParcelableExtra<Note>(EXTRA_NOTE)
        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)

         editnote = intent.getParcelableExtra<Note>(EXTRA_NOTE)
        editnoteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)

        titleView = findViewById<TextView>(R.id.title)
        textView = findViewById<TextView>(R.id.text)
        EdittitleView = findViewById<TextView>(R.id.edit_title)
        EdittextView = findViewById<TextView>(R.id.edit_text)

        titleView.text = note.title
        textView.text = note.text
        EdittitleView.setText(titleView.text)
        EdittextView.setText(textView.text)

        //EdittitleView=titleView.toString()
       // editnote.title=titleView.text.toString()
      //  editnote.text=textView.text.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_note_detail, menu)


      //  val editnoteMenu = menu?.findItem(R.id.action_edit)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                saveNote()
                toast("Note sauvegardée")
                return true
            }
            R.id.action_edit -> {
                Editnote()


                return true

            }

            R.id.action_delete -> {
                showConfirmDeleteNoteDialog()
                return true
            }
        }
            return super.onOptionsItemSelected(item)

    }

    override fun onBackPressed() {
        exitmessage()
       // super.onBackPressed()
    }
    fun exitmessage() {
        note.title = titleView.text.toString()
        note.text = textView.text.toString()
        if (note.text!!.trim()==note.filename) {
            AlertDialog.Builder(this)
                .setMessage("Voulez-vous appliquer les modification ?")
                .setPositiveButton(
                    "Oui",
                    DialogInterface.OnClickListener { dialog: DialogInterface?, i: Int ->



                    })
                .setNegativeButton(
                    "Non",
                    DialogInterface.OnClickListener { dialog: DialogInterface?, i: Int ->
                        finish()


                    })

                .show()
        } else {
            finish()
        }
    }


    fun showConfirmDeleteNoteDialog() {
        val confirmFragment = note.title?.let { ConfirmDeleteNoteDialogFragment(it) }
        confirmFragment?.listener = object :ConfirmDeleteNoteDialogFragment.ConfirmDeleteDialogListener {
            override fun onDialogPositiveClick() {
                deleteNote()
            }
            override fun onDialogNegativeClick() { }
        }

        confirmFragment?.show(supportFragmentManager, "confirmDeleteDialog")
    }

    fun saveNote() {
        note.title = titleView.text.toString()
        note.text = textView.text.toString()

        intent = Intent(ACTION_SAVE)
        intent.putExtra(EXTRA_NOTE, note as Parcelable)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    fun Editnote() {
        //note.title = EdittextView.text.toString()
       // note.text = EdittitleView.toString()


        EdittextView.visibility= GONE
        EdittitleView.visibility= GONE
        titleView.visibility= VISIBLE
        textView.visibility= VISIBLE
        toolbar.setTitle("Modifier la note")
        menu.getItem(R.menu.add_note_menu).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_baseline_edit_24))



        //startActivityForResult(intent, EditNoteActivity.REQUEST_EDIT_NOTE)
    }
        /*
            intent = Intent(ACTION_SAVE)
            intent.putExtra(EXTRA_NOTE, note as Parcelable)
            intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
            setResult(Activity.RESULT_OK, intent)
            finish()

         */



    fun deleteNote() {
        intent = Intent(ACTION_DELETE)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
fun Context.toast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}

    fun Modifier(view: View) {
        Editnote()
    }

}
