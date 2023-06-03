package com.redhaputra.movieapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.redhaputra.movieapp.common.ui.model.MovieData
import com.redhaputra.movieapp.core.network.repositories.MovieRepository
import com.redhaputra.movieapp.screen.favorite.FavoriteMovieViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Favorite movie view model test class
 */
@RunWith(JUnit4::class)
class FavoriteMovieViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var repository: MovieRepository

    private lateinit var viewModel: FavoriteMovieViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = FavoriteMovieViewModel(
            repository
        )
    }

    @Test
    fun `favoritesMovie should return value when setFavoritesMovie called`() {
        val observer = mockk<Observer<List<MovieData>>>(relaxed = true)
        val movieData = MovieData(
            id = 1234,
            backImg = "https://image.tmdb.org/t/p/w500/2I5eBh98Q4aPq8WdQrHdTC8ARhY.jpg",
            posterImg = "https://image.tmdb.org/t/p/w500/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
            overview = "overview",
            title = "title",
            review = "review"
        )
        val answer = mutableListOf(movieData)
        every { repository.loadFavoritesMovie() } answers { answer }
        viewModel.favoritesMovie.observeForever(observer)
        viewModel.setFavoritesMovie()
        verify(atLeast = 1, atMost = 1) { observer.onChanged(any()) }
        assertEquals(listOf(movieData), viewModel.favoritesMovie.value)
        viewModel.favoritesMovie.removeObserver(observer)
    }
}