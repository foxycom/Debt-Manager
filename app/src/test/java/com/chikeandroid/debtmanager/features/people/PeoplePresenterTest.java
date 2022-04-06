package com.chikeandroid.debtmanager.features.people;

import com.google.common.collect.Lists;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.chikeandroid.debtmanager.data.Person;
import com.chikeandroid.debtmanager.features.people.loader.PeopleLoader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Chike on 5/10/2017.
 * Unit tests for the implementation of {@link PeoplePresenter
 */
public class PeoplePresenterTest {

    private List<Person> mPersons;

    @Mock
    private PeopleContract.View mPeopleView;

    @Captor
    private ArgumentCaptor<List> mShowPeopleArgumentCaptor;

    @Mock
    private PeopleLoader mPeopleLoader;

    @Mock
    private LoaderManager mLoaderManager;

    private PeoplePresenter mPeoplePresenter;

    @Before
    public void setUpPeoplePresenter() {
        MockitoAnnotations.initMocks(this);

        mPeoplePresenter = new PeoplePresenter(mPeopleView, mLoaderManager, mPeopleLoader);
        Person person1 = new Person("Chike Mgbemena", "07038111534", "image_uri");
        Person person2 = new Person("Chinedu Mandu", "08047541254", "image_uri");
        Person person3 = new Person("Mary Jane", "040125789653", "image_uri");
        mPersons = Lists.newArrayList(person1, person2, person3);
    }

    @Test
    public void shouldBeAbleToLoadPeopleFromRepositoryAndLoadIntoView() {

        mPeoplePresenter.onLoadFinished(mock(Loader.class), mPersons);

        verify(mPeopleView).showPeople(mShowPeopleArgumentCaptor.capture());
        assertThat(mShowPeopleArgumentCaptor.getValue().size(), is(3));
    }

    @Test
    public void shouldBeAbleToLoadPeopleFromRepositoryAndShowEmptyViewIfNotAvailable() {

        mPeoplePresenter.onLoadFinished(mock(Loader.class), new ArrayList<Person>());

        verify(mPeopleView).showEmptyView();
    }

    @Test
    public void shouldBeAbleToShowErrorWhenPeopleIsUnavailable() {

        mPeoplePresenter.onLoadFinished(mock(Loader.class), null);

        verify(mPeopleView).showLoadingPeopleError();
    }
}
