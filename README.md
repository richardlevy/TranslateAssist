# TranslateAssist
A Java tool for merging iOS localization string files with Google Translate documents

I know that you should use a professional translation service, but for those that want to use Google Translate, this tool automates the merging of translated strings (as opposed to cut & pasting and going mad).

Because Google Translate no longer offers a free API for translation, this is a 3 stage process:

## Step 1 - Separate Out The Translations

* Take your source string file (e.g Localizable.strings) and run the tool against it as follows:

```
TranslateAssist Localizable.strings
```

This will create a file called *Localizable.strings_for_translation.txt*

## Step 2 - Translate

* Go to http://translate.google.com and select the "translate a document" option.
* Select the file generated in step 1
* Select your language options and translate
* Save the output into the same folder as the files in step 1 (for instance *translated_strings.txt*)

## Step 3 - Merge The Translations Back

* Run the tool again, this time giving it the name of the translations also:

```
TranslateAssist Localizable.strings translated_strings.txt
```

This will create a file called *Localizable.strings_translated.txt* containing the source from step 1 merged with the translations from step 2.

#Tips
Google Translate is an automated service and is not aware of the context of your sentences, or that they are part of an application.  With that in mind, follow these tips for a smoother experience

* Don't use parameterised strings (i.e %@) because you will have to manually edit the translations
* Don't use escaped characters  (e.g \n or \t).  Google will turn a "\n" into "\ N" and you will have to edit your translations
* If converting to a language such a Chinese, cuting and pasting the translation results may result in a different character set (other than UTF-16) being used.  TranslateAssist will not then be able to match up the translated strings to the original strings.  In this case, instead of cutting and pasting the translations, view the page source and copy that (removing the pre HTML tags).

