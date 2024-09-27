import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.route.todoapplication.R
import com.route.todoapplication.database.model.Task
import com.route.todoapplication.databinding.ItemTaskBinding
import com.route.todoapplication.timeFormatOnly

class TasksListAdapter(var tasks: MutableList<Task> = mutableListOf()) :
    RecyclerView.Adapter<TasksListAdapter.ViewHolder>() {

    class ViewHolder(val itemTaskBinding: ItemTaskBinding) :
        RecyclerView.ViewHolder(itemTaskBinding.root) {

        fun changeTaskStatus(isDone: Boolean) {
            if (isDone) {
                itemTaskBinding.draggingBar.setImageResource(R.drawable.dragging_bar_done)
                itemTaskBinding.title.setTextColor(Color.GREEN)
                itemTaskBinding.btnTaskIsDone.setBackgroundResource(R.drawable.done)
            } else {
                val blue = ContextCompat.getColor(itemView.context, R.color.blue)
                itemTaskBinding.title.setTextColor(blue)
                itemTaskBinding.draggingBar.setImageResource(R.drawable.dragging_bar)
                itemTaskBinding.btnTaskIsDone.setBackgroundResource(R.drawable.check_mark)
            }
        }

        fun bind(task: Task) {
            // Set task title and status in the ViewHolder
            itemTaskBinding.title.text = task.title
            changeTaskStatus(task.status)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val task = tasks[position]
//
//        // Bind the data to the views
//        holder.binding.apply {
//            title.text = task.title
//            time.text = task.time?.timeFormatOnly()
//
//            // Update the UI to reflect the current task status
//            holder.changeTaskStatus(task.status)
//
//            // Set up click listener for the "Done" button to toggle the task status
//            btnTaskIsDone.setOnClickListener {
//                task.status = !task.status  // Toggle the task's status
//                holder.changeTaskStatus(task.status)  // Update the UI
//                onDoneBtnClick?.itemClickL(position, task)  // Call the listener for any additional actions (e.g., database update)
//
//                // Notify the adapter that the item has changed so that it reflects the new state
//                notifyItemChanged(position)
//            }
//
//            // Set up click listeners for edit and delete actions
//            icDelete.setOnClickListener {
//                onDeleteListener?.itemClickL(position, task)
//            }
//            dragItem.setOnClickListener {
//                onEditClickListener?.itemClickL(position, task)
//            }
//        }
//    }
        val task = tasks[position]


        task?.let {
            holder.bind(it) // Bind the task data to the ViewHolder

            // Set task title and time
            holder.itemTaskBinding.title.text = task.title
            holder.itemTaskBinding.time.text = task.time?.timeFormatOnly()

            // Update the task status UI based on the current status
            holder.changeTaskStatus(task.status)

            // Set onClickListener for the delete button
            holder.itemTaskBinding.icDelete.setOnClickListener {
                onDeleteListener?.itemClickL(position, task)
            }

            // Set onClickListener for the edit button
            holder.itemTaskBinding.dragItem.setOnClickListener {
                onEditClickListener?.itemClickL(position, task)
            }

            // Set onClickListener for the "done" button
            holder.itemTaskBinding.btnTaskIsDone.setOnClickListener {
                onDoneBtnClick?.itemClickL(position, task)
            }
        }
    }

    fun removeTaskAt(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }


    // Adapter methods for updating the list
    fun submitNewList(newItems: MutableList<Task>) {
        tasks = newItems
        notifyDataSetChanged()
    }

    fun addItem(newItem: Task) {
        tasks.add(newItem)
        notifyItemInserted(tasks.size - 1)
    }

    // Define the listeners for edit, done, and delete
    var onEditClickListener: OnItemClickListener? = null
    var onDoneBtnClick: OnItemClickListener? = null
    var onDeleteListener: OnItemClickListener? = null

    // Listener interface
    fun interface OnItemClickListener {
        fun itemClickL(position: Int, item: Task)
    }
}





