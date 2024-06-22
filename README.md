# Developing an Annotation Tool to train an LLM to provide automatic feedback on students theses



## Getting started

To make it easy for you to get started with GitLab, here's a list of recommended next steps.

Already a pro? Just edit this README.md and make it your own. Want to make it easy? [Use the template at the bottom](#editing-this-readme)!

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://gitlab.ewi.tudelft.nl/cse2000-software-project/2023-2024/cluster-f/05c/developing-an-annotation-tool-to-train-an-llm-to-provide-automatic-feedback-on-students-theses.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

- [ ] [Set up project integrations](https://gitlab.ewi.tudelft.nl/cse2000-software-project/2023-2024/cluster-f/05c/developing-an-annotation-tool-to-train-an-llm-to-provide-automatic-feedback-on-students-theses/-/settings/integrations)

## Collaborate with your team

- [ ] [Invite team members and collaborators](https://docs.gitlab.com/ee/user/project/members/)
- [ ] [Create a new merge request](https://docs.gitlab.com/ee/user/project/merge_requests/creating_merge_requests.html)
- [ ] [Automatically close issues from merge requests](https://docs.gitlab.com/ee/user/project/issues/managing_issues.html#closing-issues-automatically)
- [ ] [Enable merge request approvals](https://docs.gitlab.com/ee/user/project/merge_requests/approvals/)
- [ ] [Set auto-merge](https://docs.gitlab.com/ee/user/project/merge_requests/merge_when_pipeline_succeeds.html)

## Test and Deploy

Use the built-in continuous integration in GitLab.

- [ ] [Get started with GitLab CI/CD](https://docs.gitlab.com/ee/ci/quick_start/index.html)
- [ ] [Analyze your code for known vulnerabilities with Static Application Security Testing (SAST)](https://docs.gitlab.com/ee/user/application_security/sast/)
- [ ] [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/ee/topics/autodevops/requirements.html)
- [ ] [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/ee/user/clusters/agent/)
- [ ] [Set up protected environments](https://docs.gitlab.com/ee/ci/environments/protected_environments.html)

***

# Editing this README

When you're ready to make this README your own, just edit this file and use the handy template below (or feel free to structure it however you want - this is just a starting point!). Thanks to [makeareadme.com](https://www.makeareadme.com/) for this template.

## Suggestions for a good README

Every project is different, so consider which of these sections apply to yours. The sections used in the template are suggestions for most open source projects. Also keep in mind that while a README can be too long and detailed, too long is better than too short. If you think your README is too long, consider utilizing another form of documentation rather than cutting out information.

## Name
Developing an Annotation Tool, used by professors to provide feedback to students' theses and utilize this feedback to train a LLM in order to provide automatic feedback.

## Description
This application should provide professors with a way to annotate PDF files, which represent students' theses. They can make use of feedback codes, which are short, predefined codes associated to some standard feedback. The professors have a dashboard, in which they can see all their assigned submissions, the last edited date of the document, the group name in which the student who uploaded the respective submission is part of, and whether the file was already submitted by the professors. Once they are done, an admin can use the parsing functionality, which retrieves the text from PDF files and applies some preprocessing techniques, in order to create an appropriate input for a Large Language Model, which will be trained in the future.

## Badges
On some READMEs, you may see small images that convey metadata, such as whether or not all the tests are passing for the project. You can use Shields to add some to your README. Many services also have instructions for adding a badge.

## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Installation
Our application does not require any local installation, since all functionality is featured on a website. The backend logic of the application should be ran on a remote server, while the frontend of the application should be uploaded to some domain.

## Usage
Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.

## Support
If you require any help using our website, please try accessing the pages related to instructions on the website, which feature detailed instructions on how to use the Annotation Tool. If you still require additional help, you can access the support page, which features a contact form and a section containing Frequently Answered Questions. We will ensure that we respond as soon as possible to your requests. If you require additional help, please feel free to reach out to us at annotationtool.sp@gmail.com. \

If, at any point, you feel that something is not running as expected, or it takes too long to register some operations, or perhaps you have any suggestions for improvement, please use the feedback form in order to reach out to us. We will make sure to provide some responses to your requests.

## Roadmap
In the future, there are some important things which should be addressed. The first of them is using the TU Delft SSO for logging in to the application, which provides another secure way to access our application, using the credentials that you are familiar with from your other accounts associated to TU Delft. \

The second thing to be done is integrating the tool with Brightspace, which would provide much easier access for professors, due to the fact that they won't have to navigate to an external tool to annotate students' theses, and would have everything accessible in one place.

## Contributing
This project is open for future contribution and should be done by developers affiliated with TU Delft. The most important future contributions have been stated above, in the Roadmap section. \

Our codebase consists of multiple packages, which contain classes responsible for some part of the logic of the whole project. If you want to contribute on the way we parse files, the first place you should check out is the ParsingService class, which is in the services package, which contains most of the logic used for extracting information out of PDF files and further processing it. \

If, instead, you want to contribute towards adding new features to the frontend of our application, all pages accessible from our tool have been defined in the frontend package. Each page contains a html file, defining the elements on the page, a css file, responsible for styling the page and a js file, connecting the elements with appropriate functionality. If you want to create new pages, consider respecting the structure of the codebase we have worked on. \

If you want to add new features to the application, which may involve altering the database, consider checking out the models package, which contains the entities from our database. Moreover, there are services responsible for modifying entities within the database, so use that functionality if you want to perform similar operations. If you decide to add new entities to the database, please follow the code structure from the repository, in order to keep it clean.

## Authors and acknowledgment
Show your appreciation to those who have contributed to the project.

## License
For open source projects, say how it is licensed.

## Project status
If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.
