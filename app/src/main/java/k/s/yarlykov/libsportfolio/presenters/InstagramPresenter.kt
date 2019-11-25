package k.s.yarlykov.libsportfolio.presenters

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import k.s.yarlykov.libsportfolio.logIt
import k.s.yarlykov.libsportfolio.repository.instagram.IInstagramAuthHelper
import k.s.yarlykov.libsportfolio.repository.instagram.IInstagramGraphHelper

class InstagramPresenter(
    private val fragment: IInstagramFragment,
    private val authHelper: IInstagramAuthHelper,
    private val graphHelper: IInstagramGraphHelper,
    private val authRequestUri: String
) : IInstagramPresenter {

    private lateinit var appSecret: String
    private lateinit var appCode: String
    private lateinit var appToken: String

    private val uriImages = mutableListOf<String>()
    private val disposable = CompositeDisposable()

    override fun onViewCreated(appSecret: String) {
        this.appSecret = appSecret
        retrieveAndRenderContent()
    }

    override fun onPause() {
        disposable.clear()
    }

    /**
     * Вызов этого кода из onViewCreated избавил от мигания экрана при переключении
     * на данный фрагмент свайпом. Мигание появлялось, если свайпом сделать следующий переход:
     * instagram -> favorites -> instagram. Если тоже самое делать тачем на заголовках таба,
     * то мигания не было. Также мигание отсутствовало при свайпе gallery -> favorites ->
     * instagram.
     */
    private fun retrieveAndRenderContent() {

        // Контент уже получен. Вывести на экран.
        // Проверка нужна потому, что TabLayout старается оптимизировать отрисовку контента
        // и вызывает onViewCreated у соседей фрагмента, который стал видимым. Чтобы при каждом
        // клике на favorites не начинать перезагрузку картинок из инстаграмма проверяем,
        // что контент уже имееется и показываем его без повторной загрузки.
        if (uriImages.isNotEmpty()) {
            updateView()

            // Контента нет, но уже получен ApplicationCode.
            // Значит можно получить токен, а затем контент
        } else if (this::appCode.isInitialized) {
            onAppCodeReceived(appCode)

            // Ещё не получен ApplicationCode.
            // Запросить аутентификацию через GUI
        } else {
            fragment.onFrontWebView()
            fragment.showAuthWebPage(authRequestUri)
        }
    }

    // Step 1.
    // Точка входа.
    // ApplicationCode либо прилетает из WebView, либо уже загружен в поле класса.
    override fun onAppCodeReceived(applicationCode: String) {

        if (this::appCode.isInitialized &&
            this::appToken.isInitialized &&
            uriImages.isNotEmpty()
        ) {
            updateView()
            return
        }

        this.appCode = applicationCode

        fragment.onFrontProgressBar()

        disposable.add(
            getMediaDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { uri -> uriImages.add(uri) },
                    { t: Throwable -> logIt("MediaDataObservable::onError '${t.message}'") },
                    { updateView() })
        )
    }

    // Step 2.
    private fun getTokenObservable(): Single<String> {

        return if (this::appToken.isInitialized) {
            Single.fromCallable {
                appToken
            }
        } else {
            authHelper
                .requestToken(appCode, appSecret)
                .map { token ->
                    token.accessToken
                }
                .doOnSuccess { t ->
                    appToken = t
                }
        }
    }

    // Step 3.
    private fun getMediaDataObservable(): Observable<String> {

        return getTokenObservable()
            .flatMapObservable { token ->
                graphHelper.requestMediaEdge(token)
            }
            .flatMap { mediaNode ->
                Observable.fromIterable(mediaNode.albums)
            }
            // Получить список медиа ресурсов в альбоме
            .flatMap { mediaAlbum ->
                graphHelper.requestMediaData(mediaAlbum.id, appToken)
            }
    }

    private fun updateView() {
        fragment.updateMediaContent(uriImages)
        fragment.onFrontRecyclerView()
    }
}