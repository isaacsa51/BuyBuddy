# Contributing

For bug reports and feature requests, please search in issues first (including the closed ones). If there are no duplicates, feel free to [submit an issue][issues] with an issue template.

**We'll probably ignore and close your issue if it's not using the existing templates or doesn't contain sufficient description.**

## Bug Report

When submitting a bug report, please make sure your issue contains **enough** information for reproducing the problem, including the options or the custom command being used, the link to the video, and other fields in the issue template.

## Feature Request

**BuyBuddy** is designed to provide users with a straightforward and effective tool for managing their finances on Android. Our focus is to offer essential budgeting functionalities in an intuitive interface, helping users track their expenses effortlessly. As such, we prioritize feature requests that align with our core mission and the fundamental principles of the app.

We appreciate the feedback and input from our users, and we are committed to continually improving **BuyBuddy** to meet the evolving needs of our user base. However, please understand that not all feature requests may be feasible or aligned with our app's mission and vision.

If you'd like to request a feature you deem necessary and useful, open a new Github issue with the Feature-Request template [here][feature-req].

## Pull Request

If you wish to contribute to the project by submitting code directly, please first leave a comment under the relevant issue or file a new issue, describe the changes you are about to make.

As per our project's guidelines, we adhere to [conventional commits][conv-commits]. Therefore, it's expected that all PRs align with this convention. Should a PR not meet these standards, we'll kindly request a review and revision.

Please use the provided Pull Request templates according to your needs. Currently, there are two to choose from:
- **General PR**: for all other PRs, including bug fixes, adding features etc.
    - ### Description
      <!-- Please include a summary of the changes and the related issue. Please also include relevant motivation and context. List any dependencies that are required for this change. -->

    - ### Related Issue
      <!--- This project only accepts pull requests related to open issues -->
      <!--- If suggesting a new feature or change, please discuss it in an issue first -->
      <!--- If fixing a bug, there should be an issue describing it with steps to reproduce -->
      <!--- Please link to the issue here: -->

    - ### Type of change 
      <!--- Is this a bug fix, adding a feature... -->

    - ### Pull Request checklist
      <!-- Before submitting the PR, please address each item. Use [x] to check the boxes -->
        - [ ] The commit message uses the [conventional commiting method][conv-commits].
        - [ ] Made sure that your PR is not duplicate
        - [ ] **Tests**: This PR includes thorough tests or an explanation of why it does not (for bug fixes/features). Your PR should pass all CI checks in our Gtihub Actions [Workflow](https://github.com/isaacsa51/BuyBuddy//actions)
        - [ ] **Screenshots**: This PR includes screenshots or GIFs of the changes made or an explanation of why it does not (*optional*)
  
- **Translation PR**: specifically to be used for translation PRs
    - ### Tanslation language
      <!--- add your translation locale here -->

       - #### Translation checklist
        <!--- 
        Translations are done by translating the base string resources (English) under:
        
        /app/src/main/res/values/strings.xml
        
        You can translate most conveniently using Android Studio's XML editor, but your preferred XML text editor works too. Explore this guide from Helpshift for Android to learn more about translation strings (https://developers.helpshift.com/android/i18n/)
         -->

      - [ ] Provide the translated strings inside strings.xml under the appropriate folder.
      - [ ] Used the correct locale code (en-US, en-GB, de, es, fr...)
      - [ ] Named the folder correctly (values-[locale-code])

> [!TIP]
>
> To avoid multiple pull requests resolving the same issue, let others know you are working on it by saying so in a comment, or ask the issue to be assigned to yourself.

## Building From Source

Fork this project, import and compile it with the latest version of [Android Studio](https://developer.android.com/studio/). 

[issues]: https://github.com/isaacsa51/BuyBuddy/issues/new/choose
[feature-req]: https://github.com/isaacsa51/BuyBuddy/issues/new?assignees=&labels=enhancement&projects=&template=--feature-request.yml&title=%5BFeature+Request%5D
[conv-commits]:https://kapeli.com/cheat_sheets/Conventional_Commits.docset/Contents/Resources/Documents/index
