# TelegramBot

Development team:

Алексей Кущенко
Ольга Филиппова
Никита Лаврентьев
Сергей Кузнецов

User functionality
Stage 0. Request definition

This is the entry point of the bot's communication with the user.

The bot greets the new user, introduces itself, and asks the user to choose a shelter:

Cat shelter Dog shelter Choosing both shelters is not allowed.

Then the bot can provide a menu to choose the user's request:

Learn about the shelter (Stage 1) How to adopt a pet from the shelter (Stage 2) Send a report about a pet (Stage 3) Call a volunteer If none of the options fit, the bot can call a volunteer.

If the user has already contacted the bot before, a new request starts with choosing the shelter the user wants to know about.

Stage 1. Consultation with a new user
At this stage, the bot should provide introductory information about the shelter: where it is located, how and when it operates, what are the rules for entering the shelter area, rules for being inside and communicating with the animal. The functionality for the cat and dog shelters is identical, but the information inside will be different, as the shelters are located in different places and have different restrictions and rules for being with animals. Important: the database for users of different shelters should be different.

The bot greets the user. The bot can tell about the shelter. The bot can provide the schedule and address of the shelter, as well as driving directions. The bot can provide the contact details of security to arrange a pass for a car. The bot can provide general safety recommendations on the shelter premises. The bot can accept and record contact information for communication. If the bot cannot answer the client's questions, a volunteer can be called.

Stage 2. Consultation with a potential adopter from a shelter.
At this stage, the bot helps potential animal adopters from a shelter to understand bureaucratic (contractual formalities) and household (how to prepare for life with an animal) questions.

Main task: to provide the most complete information on how a person should prepare to meet their new family member.

The bot greets the user. The bot can provide rules for getting to know the animal before taking it from the shelter. The bot can provide a list of documents necessary to take an animal from the shelter. The bot can provide a list of recommendations for transporting the animal. The bot can provide a list of recommendations for arranging a home for a puppy/kitten. The bot can provide a list of recommendations for arranging a home for an adult animal. The bot can provide a list of recommendations for arranging a home for an animal with limited abilities (vision, mobility). The bot can provide advice from a dog trainer on initial communication with a dog (not applicable to a cat shelter, to be implemented only for a dog shelter). The bot can provide recommendations for trusted dog trainers for future reference (not applicable to a cat shelter, to be implemented only for a dog shelter). The bot can provide a list of reasons why an application to adopt a dog may be declined and the dog not be released from the shelter. The bot can collect and record contact information for communication. (Important: the user database for different shelters must be different.) If the bot is unable to answer the client's questions, a volunteer can be summoned.

Stage 3. Pet Care
After the new owner adopts a pet from the shelter, they are required to provide information on how the animal is doing in its new home within a month. The daily report should include the following information:

A photo of the animal. The pet's diet. The overall well-being and adjustment to the new environment. Changes in behavior, such as giving up old habits or acquiring new ones. The report should be submitted every day, and there is no time limit for submitting it. Volunteers review all reports sent after 9:00 p.m. every day. If the adopter does not fill out the report thoroughly, the volunteer can provide feedback via the bot, stating, "Dear adopter, we noticed that you are not filling out the report as thoroughly as necessary. Please take this task more seriously. Otherwise, shelter volunteers will have to personally check on the conditions in which the animal is being kept."

New adopters are entered into the database by a volunteer. There is one database for cat shelter adopters and another for dog shelter adopters.

The bot's task is to accept information and remind users to send reports if they have not done so. If more than two days have passed since the last report, the bot will contact the volunteer to get in touch with the adopter.

After the 30-day period ends, volunteers decide whether the animal stays with the owner or not. The probationary period may be completed, extended for another 14 or 30 days, or not passed.

The bot can perform the following actions:

Send a daily report form. Request text if the user only sends a photo. Request a photo if the user only sends text. Issue a warning message if the report is not filled out properly (as done by volunteers): "Dear adopter, we noticed that you are not filling out the report as thoroughly as necessary. Please take this task more seriously. Otherwise, shelter volunteers will have to personally check on the conditions in which the animal is being kept." Congratulate the adopter with a standard message if the probationary period has been completed. Notify the adopter and provide instructions on next steps if the probationary period has not been passed. Contact the volunteer if the bot is unable to answer the user's questions.