package tmg.flashback.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tmg.flashback.base.BaseViewModel
import tmg.flashback.repo.db.news.NewsDB
import tmg.flashback.repo.models.news.NewsItem

//region Inputs

interface NewsViewModelInputs {

}

//endregion

//region Outputs

interface NewsViewModelOutputs {
    val news: LiveData<List<NewsItem>>
}

//endregion

class NewsViewModel(
    newsDB: NewsDB
): BaseViewModel(), NewsViewModelInputs, NewsViewModelOutputs {

    var inputs: NewsViewModelInputs = this
    var outputs: NewsViewModelOutputs = this

    override val news: LiveData<List<NewsItem>> = newsDB
        .getNews()
        .filter { it.code == 200 && it.result != null }
        .map { it.result!! }
        .asLiveData(viewModelScope.coroutineContext)

    init {

    }

    //region Inputs

    //endregion
}