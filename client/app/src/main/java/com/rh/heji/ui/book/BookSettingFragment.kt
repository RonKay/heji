package com.rh.heji.ui.book

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ClickUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.util.XPopupUtils
import com.rh.heji.*
import com.rh.heji.data.converters.DateConverters
import com.rh.heji.data.db.Book
import com.rh.heji.data.db.BookUser
import com.rh.heji.databinding.FragmentBookSettingBinding
import com.rh.heji.databinding.ItemBookUsersBinding
import com.rh.heji.ui.base.BaseFragment
import com.rh.heji.ui.book.pop.BottomSharePop

class BookSettingFragment : BaseFragment() {

    private val viewModel by lazy { getViewModel(BookViewModel::class.java) }
    private lateinit var binding: FragmentBookSettingBinding
    private lateinit var book: Book

    override fun layoutId(): Int {
        return R.layout.fragment_book_setting
    }

    override fun setUpToolBar() {
        super.setUpToolBar()
        toolBar.title = "账本设置"
        showBlack()
    }

    override fun initView(rootView: View) {
        binding = FragmentBookSettingBinding.bind(rootView)

        arguments?.let {
            book = BookSettingFragmentArgs.fromBundle(it).book
            binding.tvBookType.text = book.type
            val createTime = DateConverters.date2Str(book.id.getObjectTime())
            binding.tvCreateTime.text = createTime
            val adapter = UsersAdapter(mutableListOf())
            viewModel.getBookUsers(book.id) { bookUsers ->
                adapter.addData(bookUsers)
            }
            binding.recycler.adapter = adapter
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())
            //binding.tvCreateUser.text = book.createUser
        }
        clearBook()
        deleteBook()
        categoryManager()
        addBookUser()
    }

    private fun clearBook() {
        binding.tvClearBill.setOnClickListener {

        }
    }

    private fun deleteBook() {
        binding.tvDeleteBook.setOnClickListener {

        }
    }

    private fun categoryManager() {
        binding.tvCategoryManager.setOnClickListener {

        }
    }

    private fun addBookUser() {
        //防止多次点击
        val clickListener = object : ClickUtils.OnDebouncingClickListener() {
            override fun onDebouncingClick(v: View?) {
                viewModel.addBookUser(bookId = book.id) {
                    XPopup.Builder(requireContext()).asCustom(BottomSharePop(requireContext(), it))
                        .show()
                }
            }
        }
        binding.tvAddBookUser.setOnClickListener(clickListener)

    }

    inner class UsersAdapter(users: MutableList<BookUser>) :
        BaseQuickAdapter<BookUser, BaseViewHolder>(
            layoutResId = R.layout.item_book_users, data = users
        ) {
        override fun convert(holder: BaseViewHolder, bookUser: BookUser) {
            val itemBinding = ItemBookUsersBinding.bind(holder.itemView);
            itemBinding.tvCreateUser.text = bookUser.name
            itemBinding.tvAuthtroy.text = bookUser.fromAuthority()

        }

    }
}