package com.taskmaster.aayushf.taskmaster

import android.graphics.Typeface
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.mikepenz.fastadapter.items.AbstractItem
import io.realm.Realm
import io.realm.RealmResults
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by aayushf on 13/6/17.
 */
class TaskViewItem(var task: Task?) : AbstractItem<TaskViewItem, TaskViewItem.ViewHolder>() {
    val sdf: SimpleDateFormat = SimpleDateFormat("EEE - dd MMM")
    var taskfont: Typeface? = null
    var tagfont: Typeface? = null
    var datesfont: Typeface? = null
    var taskpk: Int? = task?.primk

    override fun getType(): Int {
        return 0
    }


    override fun getLayoutRes(): Int {
        return R.layout.cardlistitem
    }

    override fun bindView(holder: ViewHolder?, payloads: MutableList<Any>?) {
        super.bindView(holder, payloads)
        holder?.tvtask?.text = "${task?.task}"
        holder?.tvtask?.typeface = taskfont
        holder?.cv?.backgroundColor = Task.colours[task!!.colour]
        holder?.tvtag?.text = "{cmd_tag_text_outline}${task?.tag}"
        holder?.tvtag?.typeface = tagfont
        holder?.tvpoints?.text = "{cmd_star_circle}${task?.points.toString()}"
        holder?.tvpoints?.typeface = taskfont
        holder?.tvdateadded?.text = "{cmd_calendar_plus} ${sdf.format(Date((task as Task).dateadded))}"
        holder?.tvdateadded?.typeface = datesfont
        holder?.tvdatepending?.typeface = datesfont
        val datependingstr: String = when {
            (task as Task).datepending != null -> sdf.format(Date((task as Task).datepending!!))
            else -> "Not Specified"

        }
        holder?.tvtask?.onClick {
            if (holder.btndonecv?.visibility == View.GONE) {
                holder.btndonecv.visibility = View.VISIBLE
                holder.btndeletecv?.visibility = View.VISIBLE
            } else {
                holder.btndonecv?.visibility = View.GONE
                holder.btndeletecv?.visibility = View.GONE

            }


        }
        holder?.tvdatepending?.text = "{cmd_calendar_clock} ${datependingstr}"
        holder?.btndeletecv?.onClick {
            val r: Realm = Realm.getDefaultInstance()
            val results: RealmResults<Task> = r.where(Task::class.java).equalTo("task", (task as Task).task).findAll()
            r.beginTransaction()
            results.deleteAllFromRealm()
            r.commitTransaction()

        }




        holder?.btndonecv?.onClick {
            val r: Realm = Realm.getDefaultInstance()
            r.beginTransaction()
            (task as Task).done = true
            r.copyToRealmOrUpdate(task)
            r.commitTransaction()
        }

    }

    override fun getViewHolder(v: View?): ViewHolder {
        return ViewHolder(v)
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        val tvtask: TextView? = itemView?.find(R.id.tvtaskcard)
        val cv: CardView? = itemView?.find(R.id.cvitem)
        val tvtag: TextView? = itemView?.find(R.id.tvtagcard)
        val tvpoints: TextView? = itemView?.find(R.id.tvpointscard)
        val btndonecv: Button? = itemView?.find(R.id.btndonecard)
        val tvdateadded: TextView? = itemView?.find(R.id.dateaddedtvcard)
        val tvdatepending: TextView? = itemView?.find(R.id.datependingtvcard)
        val btndeletecv: Button? = itemView?.find(R.id.btndeletecard)


    }

    companion object {
        fun getListOfTaskView(r: RealmResults<Task>): MutableList<TaskViewItem> {
            var listoftv: MutableList<TaskViewItem> = arrayListOf()
            r.forEach { t: Task? ->
                listoftv.add(TaskViewItem(t))
            }
            Log.d("RealmResults", r.size.toString())
            Log.d("Get ListOfTaskView", "$listoftv")
            return listoftv
        }

    }


}