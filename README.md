
# StackLayout
---

This library is intended to replace fragment stack, popups and dialogs with plain old views.

We can do it stupid simple. And in a much more flexible way. With less boilerplate code.
And less bugs.

Have you ever seen the source code that is used just to show a single FragmentDialog?
All that support libraries, all that fragments, window managers, and so on?
At first developer could think that such complexities are mandatory to provide a developer
with a great flexibility.

But later that developer could try to set a width for such dialog.
What a pain! No single documented solution for doing such a simple task! Workarounds,
unreliable code - this is how it is doing to end.

Then a developer
could try to pass some data from a dialog back to a calling fragment. WHAT? There is NO way for
doing such a simple task? The framework just does not guarantee that a calling fragment is still exist in the
stack after a couple of screen flips or other in-app actions.

The next thing that comes to mind - fragment transition animations. A developer wants to replace
fragment A with fragment B and he wants fragment B to fly nicely in over fragment A. OK, we have
this `setCustomAnimations` method, so let's use it. Everything is looking so simple - just set
that animations and done! Not that simple! Fragment B will go BELOW fragment A. Whatever developer
does, it will ALWAYS go below. So, forget about nice transition animations.

Is this enough? Pass some arguments from one fragment to another. Looks simple - take a fragment,
call a method. But wait! Nothing is going to be simple in the fragments world. You should use
**Bundles** and increase your passing code's size up to 5 times! Why?
There is no real reason for a such limitation, but docs are advising
against using simpler methods, so even if you know that it is fine, you will feel some discomfort
going against the recommended way, especially if you're beginner and do not know how exactly
all of that work.

The next nonsense is Loader. Loader aims to provide fragment with data, preserving it's state during
configuration change. You're passing arguments in a Bundle (again?) and that arguments will be lost after
process recreation so you will need to save that arguments somehow to restart the loader if needed. But be careful
to not restart it while it is still running. Sounds intricately, isn't it? Yes it is! You can do
this job by keeping requested data in a simple static variable (a singleton, Dagger - you name it) with the much ease and without all that overloads
and overcomplicated classes. Another way to go is the Model-View-Presenter pattern that becomes so popular over last months.

The next thing to discuss is the Fragment's lifecycle. I can't say more than these guys:
[Advocating Against Android Fragments](http://corner.squareup.com/2014/10/advocating-against-android-fragments.html)
You're just increasing your code complexity without any advantages. I mean, no ANY architecture advantages
for using fragments instead of custom layouts.

I've spent some time trying to figure out what is a real reason
for fragments to exist, and I've found only one real reason: the fragment stack.
The stack that freezes bottom views that are not observable
by the user and automatically inflates and restores them again when the user wants to return.
If we could only replace that fragment stack, we're done!

A first look at the library
that targets to replace the fragment stack [Flow](https://github.com/square/flow) is a proof
that this problem could be solved with a little amount of code. But I didn't want such a bulky solution just for
the stack. I wanted a solution for the stack, dialogs and popups. And such solution
should allow me to write **less** code than I do with fragments. Every time I write an
utility function or include a library I believe that I do this to write **less** code in the future,
so Flow library, while it is still better than fragments, is not acceptable for me.

# About this release
---

This is my first early-pre-alpha-pre-snapshot release of this library.
It is not even published to Maven Central yet.
I'm working on my first project with this library, so the code will evolve much - I can not
guarantee any compatibility with future versions.

You could try this library, and if you find it useful, I would like to hear any feedback
or improvement suggestions.

# How-tos and example
---

Look into example to understand how it works. Later I will add some examples with pictures,
so if you're not getting it, just come back a month later.

Key example elements:

``` java
        // adding a view to a stack
        viewStack.push(R.layout.screen_note_list);

        // adding a view and passing some arguments
        NoteScreen noteScreen = viewStack.push(R.layout.screen_note);
        noteScreen.setItemText(text);

        // passing some data from a dialog back to the calling view
        DeleteDialogOwner owner = viewStack.findBackView(this, DeleteDialogOwner.class);
        viewStack.pop(this);
        owner.onDeleteButtonOK(this);
```

# Contact information
---

Contact me with email: sirstripy-at-gmail-com or file an issue!

