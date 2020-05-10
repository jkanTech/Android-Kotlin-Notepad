package com.jkantech.monblocnotes

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.jkantech.monblocnotes.utils.deleteNote
import kotlinx.android.synthetic.main.add_notes.*

class EditNoteActivity: AppCompatActivity() {

    companion object {
        val REQUEST_EDIT_NOTE = 1

        val EXTRA_NOTE = "note"
        val EXTRA_NOTE_INDEX = "noteIndex"

        val ACTION_SAVE = "com.jkantech.monblocnotes.actions.ACTION_SAVE"
        val ACTION_DELETE = "com.jkantech.monblocnotes.action.ACTION_DELETE"

    }

    lateinit var note: Note
    var noteIndex: Int = -1
    lateinit var titleView:TextView
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        toolbar.setTitle("Modifier la note")

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        note = intent.getParcelableExtra<Note>(EXTRA_NOTE)
        noteIndex = intent.getIntExtra(EXTRA_NOTE_INDEX, -1)

        titleView = findViewById<TextView>(R.id.title)
        textView = findViewById<TextView>(R.id.text)

        titleView.text = note.title
        textView.text = note.text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_note_detail, menu)
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
        note.title = titleView.text.toString()
        note.text = textView.text.toString()
        intent = Intent(ACTION_SAVE)
        intent.putExtra(EXTRA_NOTE, note as Parcelable)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()

    }

    fun deleteNote() {
        intent = Intent(ACTION_DELETE)
        intent.putExtra(EXTRA_NOTE_INDEX, noteIndex)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    fun Context.toast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

}
