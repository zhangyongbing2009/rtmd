<?php
// Return a list of available months (or can be used for hours when AM/PM designated)
function getMonths($month) {
	if ($month == '01')
	 echo "<option value='01' selected='selected'>01</option>";
	else
	 echo "<option value='01'>01</option>";
	if ($month == '02')
	 echo "<option value='02' selected='selected'>02</option>";
	else
	 echo "<option value='02'>02</option>";
	if ($month == '03')
	 echo "<option value='03' selected='selected'>03</option>";
	else
	 echo "<option value='03'>03</option>";
	if ($month == '04')
	 echo "<option value='04' selected='selected'>04</option>";
	else
	 echo "<option value='04'>04</option>";
	if ($month == '05')
	 echo "<option value='05' selected='selected'>05</option>";
	else
	 echo "<option value='05'>05</option>";
	if ($month == '06')
	 echo "<option value='06' selected='selected'>06</option>";
	else
	 echo "<option value='06'>06</option>";
	if ($month == '07')
	 echo "<option value='07' selected='selected'>07</option>";
	else
	 echo "<option value='07'>07</option>";
	if ($month == '08')
	 echo "<option value='08' selected='selected'>08</option>";
	else
	 echo "<option value='08'>08</option>";
	if ($month == '09')
	 echo "<option value='09' selected='selected'>09</option>";
	else
	 echo "<option value='09'>09</option>";
	if ($month == 10)
	 echo "<option value='10' selected='selected'>10</option>";
	else
	 echo "<option value='10'>10</option>";
	if ($month == 11)
	 echo "<option value='11' selected='selected'>11</option>";
	else
	 echo "<option value='11'>11</option>";
	if ($month == 12)
	 echo "<option value='12' selected='selected'>12</option>";
	else
	 echo "<option value='12'>12</option>";	 	  	 	 	 	 	 	 
}

// Return a list of available days
function getDays($day) {
	if ($day == '01')
	 echo "<option value='01' selected='selected'>01</option>";
	else
	 echo "<option value='01'>01</option>";
	if ($day == '02')
	 echo "<option value='02' selected='selected'>02</option>";
	else
	 echo "<option value='02'>02</option>";
	if ($day == '03')
	 echo "<option value='03' selected='selected'>03</option>";
	else
	 echo "<option value='03'>03</option>";
	if ($day == '04')
	 echo "<option value='04' selected='selected'>04</option>";
	else
	 echo "<option value='04'>04</option>";
	if ($day == '05')
	 echo "<option value='05' selected='selected'>05</option>";
	else
	 echo "<option value='05'>05</option>";
	if ($day == '06')
	 echo "<option value='06' selected='selected'>06</option>";
	else
	 echo "<option value='06'>06</option>";
	if ($day == '07')
	 echo "<option value='07' selected='selected'>07</option>";
	else
	 echo "<option value='07'>07</option>";
	if ($day == '08')
	 echo "<option value='08' selected='selected'>08</option>";
	else
	 echo "<option value='08'>08</option>";
	if ($day == '09')
	 echo "<option value='09' selected='selected'>09</option>";
	else
	 echo "<option value='09'>09</option>";
	if ($day == 10)
	 echo "<option value='10' selected='selected'>10</option>";
	else
	 echo "<option value='10'>10</option>";
	if ($day == 11)
	 echo "<option value='11' selected='selected'>11</option>";
	else
	 echo "<option value='11'>11</option>";
	if ($day == 12)
	 echo "<option value='12' selected='selected'>12</option>";
	else
	 echo "<option value='12'>12</option>";
	if ($day == 13)
	 echo "<option value='13' selected='selected'>13</option>";
	else
	 echo "<option value='13'>13</option>";
	if ($day == 14)
	 echo "<option value='14' selected='selected'>14</option>";
	else
	 echo "<option value='14'>14</option>";
	if ($day == 15)
	 echo "<option value='15' selected='selected'>15</option>";
	else
	 echo "<option value='15'>15</option>";
	if ($day == 16)
	 echo "<option value='16' selected='selected'>16</option>";
	else
	 echo "<option value='16'>16</option>";
	if ($day == 17)
	 echo "<option value='17' selected='selected'>17</option>";
	else
	 echo "<option value='17'>17</option>";
	if ($day == 18)
	 echo "<option value='18' selected='selected'>18</option>";
	else
	 echo "<option value='18'>18</option>";
	if ($day == 19)
	 echo "<option value='19' selected='selected'>19</option>";
	else
	 echo "<option value='19'>19</option>";
	if ($day == 20)
	 echo "<option value='20' selected='selected'>20</option>";
	else
	 echo "<option value='20'>20</option>";
	if ($day == 21)
	 echo "<option value='21' selected='selected'>21</option>";
	else
	 echo "<option value='21'>21</option>";
	if ($day == 22)
	 echo "<option value='22' selected='selected'>22</option>";
	else
	 echo "<option value='22'>22</option>";
	if ($day == 23)
	 echo "<option value='23' selected='selected'>23</option>";
	else
	 echo "<option value='23'>23</option>";
	if ($day == 24)
	 echo "<option value='24' selected='selected'>24</option>";
	else
	 echo "<option value='24'>24</option>";
	if ($day == 25)
	 echo "<option value='25' selected='selected'>25</option>";
	else
	 echo "<option value='25'>25</option>";
	if ($day == 26)
	 echo "<option value='26' selected='selected'>26</option>";
	else
	 echo "<option value='26'>26</option>";
	if ($day == 27)
	 echo "<option value='27' selected='selected'>27</option>";
	else
	 echo "<option value='27'>27</option>";
	if ($day == 28)
	 echo "<option value='28' selected='selected'>28</option>";
	else
	 echo "<option value='28'>28</option>";
    if ($day == 29)
	 echo "<option value='29' selected='selected'>29</option>";
	else
	 echo "<option value='29'>29</option>";
    if ($day == 30)
	 echo "<option value='30' selected='selected'>30</option>";
	else
	 echo "<option value='30'>30</option>";
	if ($day == 31)
	 echo "<option value='31' selected='selected'>31</option>";
	else
	 echo "<option value='31'>31</option>";	
}

// Return a list of available years
function getYears($year) {
	if ($year == 1990)
	 echo "<option value='1990' selected='selected'>1990</option>";
	else
	 echo "<option value='1990'>1990</option>";
	if ($year == 1991)
	 echo "<option value='1991' selected='selected'>1991</option>";
	else
	 echo "<option value='1991'>1991</option>";
	if ($year == 1992)
	 echo "<option value='1992' selected='selected'>1992</option>";
	else
	 echo "<option value='1992'>1992</option>";
	if ($year == 1993)
	 echo "<option value='1993' selected='selected'>1993</option>";
	else
	 echo "<option value='1993'>1993</option>";
	if ($year == 1994)
	 echo "<option value='1994' selected='selected'>1994</option>";
	else
	 echo "<option value='1994'>1994</option>";
	if ($year == 1995)
	 echo "<option value='1995' selected='selected'>1995</option>";
	else
	 echo "<option value='1995'>1995</option>";
	if ($year == 1996)
	 echo "<option value='1996' selected='selected'>1996</option>";
	else
	 echo "<option value='1996'>1996</option>";
	if ($year == 1997)
	 echo "<option value='1997' selected='selected'>1997</option>";
	else
	 echo "<option value='1997'>1997</option>";
	if ($year == 1998)
	 echo "<option value='1998' selected='selected'>1998</option>";
	else
	 echo "<option value='1998'>1998</option>";
	if ($year == 1999)
	 echo "<option value='1999' selected='selected'>1999</option>";
	else
	 echo "<option value='1999'>1999</option>";
	if ($year == 2000)
	 echo "<option value='2000' selected='selected'>2000</option>";
	else
	 echo "<option value='2000'>2000</option>";
	if ($year == 2001)
	 echo "<option value='2001' selected='selected'>2001</option>";
	else
	 echo "<option value='2001'>2001</option>";
	if ($year == 2002)
	 echo "<option value='2002' selected='selected'>2002</option>";
	else
	 echo "<option value='2002'>2002</option>";
	if ($year == 2003)
	 echo "<option value='2003' selected='selected'>2003</option>";
	else
	 echo "<option value='2003'>2003</option>";
	if ($year == 2004)
	 echo "<option value='2004' selected='selected'>2004</option>";
	else
	 echo "<option value='2004'>2004</option>";
	if ($year == 2005)
	 echo "<option value='2005' selected='selected'>2005</option>";
	else
	 echo "<option value='2005'>2005</option>";
	if ($year == 2006)
	 echo "<option value='2006' selected='selected'>2006</option>";
	else
	 echo "<option value='2006'>2006</option>";
	if ($year == 2007)
	 echo "<option value='2007' selected='selected'>2007</option>";
	else
	 echo "<option value='2007'>2007</option>";
	if ($year == 2008)
	 echo "<option value='2008' selected='selected'>2008</option>";
	else
	 echo "<option value='2008'>2008</option>";
	if ($year == 2009)
	 echo "<option value='2009' selected='selected'>2009</option>";
	else
	 echo "<option value='2009'>2009</option>";
	if ($year == 2010)
	 echo "<option value='2010' selected='selected'>2010</option>";
	else
	 echo "<option value='2010'>2010</option>";
	if ($year == 2011)
	 echo "<option value='2011' selected='selected'>2011</option>";
	else
	 echo "<option value='2011'>2011</option>";
	if ($year == 2012)
	 echo "<option value='2012' selected='selected'>2012</option>";
	else
	 echo "<option value='2012'>2012</option>";
	if ($year == 2013)
	 echo "<option value='2013' selected='selected'>2013</option>";
	else
	 echo "<option value='2013'>2013</option>";
	if ($year == 2014)
	 echo "<option value='2014' selected='selected'>2014</option>";
	else
	 echo "<option value='2014'>2014</option>";
	if ($year == 2015)
	 echo "<option value='2015' selected='selected'>2015</option>";
	else
	 echo "<option value='2015'>2015</option>";
	if ($year == 2016)
	 echo "<option value='2016' selected='selected'>2016</option>";
	else
	 echo "<option value='2016'>2016</option>";
	if ($year == 2017)
	 echo "<option value='2017' selected='selected'>2017</option>";
	else
	 echo "<option value='2017'>2017</option>";
	if ($year == 2018)
	 echo "<option value='2018' selected='selected'>2018</option>";
	else
	 echo "<option value='2018'>2018</option>";
	if ($year == 2019)
	 echo "<option value='2019' selected='selected'>2019</option>";
	else
	 echo "<option value='2019'>2019</option>";
	if ($year == 2020)
	 echo "<option value='2020' selected='selected'>2020</option>";
	else
	 echo "<option value='2020'>2020</option>";
	if ($year == 2021)
	 echo "<option value='2021' selected='selected'>2021</option>";
	else
	 echo "<option value='2021'>2021</option>";
	if ($year == 2022)
	 echo "<option value='2022' selected='selected'>2022</option>";
	else
	 echo "<option value='2022'>2022</option>";
	if ($year == 2023)
	 echo "<option value='2023' selected='selected'>2023</option>";
	else
	 echo "<option value='2023'>2023</option>";
	if ($year == 2024)
	 echo "<option value='2024' selected='selected'>2024</option>";
	else
	 echo "<option value='2024'>2024</option>";
	if ($year == 2025)
	 echo "<option value='2025' selected='selected'>2025</option>";
	else
	 echo "<option value='2025'>2025</option>";
}

// Return a list of available Hours
function getHours($hour) {
	echo "the hour sent is $hour";
	if ($hour == '00')
	 echo "<option value='00' selected='selected'>00</option>";
 	else
	 echo "<option value='00'>00</option>";
	if ($hour == '01')
	 echo "<option value='01' selected='selected'>01</option>";
	else
	 echo "<option value='01'>01</option>";	 
	if ($hour == '02')
	 echo "<option value='02' selected='selected'>02</option>";
	else
	 echo "<option value='02'>02</option>";
	if ($hour == '03')
	 echo "<option value='03' selected='selected'>03</option>";
	else
	 echo "<option value='03'>03</option>";	 
	if ($hour == '04')
	 echo "<option value='04' selected='selected'>04</option>";
	else
	 echo "<option value='04'>04</option>";
	if ($hour == '05')
	 echo "<option value='05' selected='selected'>05</option>";
	else
	 echo "<option value='05'>05</option>";	 
	if ($hour == '06')
	 echo "<option value='06' selected='selected'>06</option>";
	else
	 echo "<option value='06'>06</option>";
	if ($hour == '07')
	 echo "<option value='07' selected='selected'>07</option>";
	else
	 echo "<option value='07'>07</option>";	 
	if ($hour == '08')
	 echo "<option value='08' selected='selected'>08</option>";
	else
	 echo "<option value='08'>08</option>";
	if ($hour == '09')
	 echo "<option value='09' selected='selected'>09</option>";
	else
	 echo "<option value='09'>09</option>";	 
	if ($hour == 10)
	 echo "<option value='10' selected='selected'>10</option>";
	else
	 echo "<option value='10'>10</option>";
	if ($hour == 11)
	 echo "<option value='11' selected='selected'>11</option>";
	else
	 echo "<option value='11'>11</option>";	 
	if ($hour == 12)
	 echo "<option value='12' selected='selected'>12</option>";
	else
	 echo "<option value='12'>12</option>";
	if ($hour == 13)
	 echo "<option value='13' selected='selected'>13</option>";
	else
	 echo "<option value='13'>13</option>";	 
	if ($hour == 14)
	 echo "<option value='14' selected='selected'>14</option>";
	else
	 echo "<option value='14'>14</option>";
	if ($hour == 15)
	 echo "<option value='15' selected='selected'>15</option>";
	else
	 echo "<option value='15'>15</option>";	 
	if ($hour == 16)
	 echo "<option value='16' selected='selected'>16</option>";
	else
	 echo "<option value='16'>16</option>";
	if ($hour == 17)
	 echo "<option value='17' selected='selected'>17</option>";
	else
	 echo "<option value='17'>17</option>";	 
	if ($hour == 18)
	 echo "<option value='18' selected='selected'>18</option>";
	else
	 echo "<option value='18'>18</option>";
	if ($hour == 19)
	 echo "<option value='19' selected='selected'>19</option>";
	else
	 echo "<option value='19'>19</option>";	 
	if ($hour == 20)
	 echo "<option value='20' selected='selected'>20</option>";
	else
	 echo "<option value='20'>20</option>";
	if ($hour == 21)
	 echo "<option value='21' selected='selected'>21</option>";
	else
	 echo "<option value='21'>21</option>";	 
	if ($hour == 22)
	 echo "<option value='22' selected='selected'>22</option>";
	else
	 echo "<option value='22'>22</option>";
	if ($hour == 23)
	 echo "<option value='23' selected='selected'>23</option>";
}

// Return a list of available minutes (or can be used for seconds)
function getMinutes($minute) {
	if ($minute == '00')
	 echo "<option value='00' selected='selected'>00</option>";
	else
	 echo "<option value='00'>00</option>";
	if ($minute == '01')
	 echo "<option value='01' selected='selected'>01</option>";
	else
	 echo "<option value='01'>01</option>";	 
	if ($minute == '02')
	 echo "<option value='02' selected='selected'>02</option>";
	else
	 echo "<option value='02'>02</option>";
	if ($minute == '03')
	 echo "<option value='03' selected='selected'>03</option>";
	else
	 echo "<option value='03'>03</option>";	 
	if ($minute == '04')
	 echo "<option value='04' selected='selected'>04</option>";
	else
	 echo "<option value='04'>04</option>";
	if ($minute == '05')
	 echo "<option value='05' selected='selected'>05</option>";
	else
	 echo "<option value='05'>05</option>";	 
	if ($minute == '06')
	 echo "<option value='06' selected='selected'>06</option>";
	else
	 echo "<option value='06'>06</option>";
	if ($minute == '07')
	 echo "<option value='07' selected='selected'>07</option>";
	else
	 echo "<option value='07'>07</option>";	 
	if ($minute == '08')
	 echo "<option value='08' selected='selected'>08</option>";
	else
	 echo "<option value='08'>08</option>";
	if ($minute == '09')
	 echo "<option value='09' selected='selected'>09</option>";
	else
	 echo "<option value='09'>09</option>";	 
	if ($minute == 10)
	 echo "<option value='10' selected='selected'>10</option>";
	else
	 echo "<option value='10'>10</option>";
	if ($minute == 11)
	 echo "<option value='11' selected='selected'>11</option>";
	else
	 echo "<option value='11'>11</option>";	 
	if ($minute == 12)
	 echo "<option value='12' selected='selected'>12</option>";
	else
	 echo "<option value='12'>12</option>";
	if ($minute == 13)
	 echo "<option value='13' selected='selected'>13</option>";
	else
	 echo "<option value='13'>13</option>";	 
	if ($minute == 14)
	 echo "<option value='14' selected='selected'>14</option>";
	else
	 echo "<option value='14'>14</option>";
	if ($minute == 15)
	 echo "<option value='15' selected='selected'>15</option>";
	else
	 echo "<option value='15'>15</option>";	 
	if ($minute == 16)
	 echo "<option value='16' selected='selected'>16</option>";
	else
	 echo "<option value='16'>16</option>";
	if ($minute == 17)
	 echo "<option value='17' selected='selected'>17</option>";
	else
	 echo "<option value='17'>17</option>";	 
	if ($minute == 18)
	 echo "<option value='18' selected='selected'>18</option>";
	else
	 echo "<option value='18'>18</option>";
	if ($minute == 19)
	 echo "<option value='19' selected='selected'>19</option>";
	else
	 echo "<option value='19'>19</option>";	 
	if ($minute == 20)
	 echo "<option value='20' selected='selected'>20</option>";
	else
	 echo "<option value='20'>20</option>";
	if ($minute == 21)
	 echo "<option value='21' selected='selected'>21</option>";
	else
	 echo "<option value='21'>21</option>";	 
	if ($minute == 22)
	 echo "<option value='22' selected='selected'>22</option>";
	else
	 echo "<option value='22'>22</option>";
	if ($minute == 23)
	 echo "<option value='23' selected='selected'>23</option>";
	else
	 echo "<option value='23'>23</option>";	 
	if ($minute == 24)
	 echo "<option value='24' selected='selected'>24</option>";
	else
	 echo "<option value='24'>24</option>";
	if ($minute == 25)
	 echo "<option value='25' selected='selected'>25</option>";
	else
	 echo "<option value='25'>25</option>";	 
	if ($minute == 26)
	 echo "<option value='26' selected='selected'>26</option>";
	else
	 echo "<option value='26'>26</option>";
	if ($minute == 27)
	 echo "<option value='27' selected='selected'>27</option>";
	else
	 echo "<option value='27'>27</option>";	 
	if ($minute == 28)
	 echo "<option value='28' selected='selected'>28</option>";
	else
	 echo "<option value='28'>28</option>";
	if ($minute == 29)
	 echo "<option value='29' selected='selected'>29</option>";
	else
	 echo "<option value='29'>29</option>";	 
	if ($minute == 30)
	 echo "<option value='30' selected='selected'>30</option>";
	else
	 echo "<option value='30'>30</option>";
	if ($minute == 31)
	 echo "<option value='31' selected='selected'>31</option>";
	else
	 echo "<option value='31'>31</option>";	 
	if ($minute == 32)
	 echo "<option value='32' selected='selected'>32</option>";
	else
	 echo "<option value='32'>32</option>";
	if ($minute == 33)
	 echo "<option value='33' selected='selected'>33</option>";
	else
	 echo "<option value='33'>33</option>";	 
	if ($minute == 34)
	 echo "<option value='34' selected='selected'>34</option>";
	else
	 echo "<option value='34'>34</option>";
	if ($minute == 35)
	 echo "<option value='35' selected='selected'>35</option>";
	else
	 echo "<option value='35'>35</option>";	 
	if ($minute == 36)
	 echo "<option value='36' selected='selected'>36</option>";
	else
	 echo "<option value='36'>36</option>";
	if ($minute == 37)
	 echo "<option value='37' selected='selected'>37</option>";
	else
	 echo "<option value='37'>37</option>";	 
	if ($minute == 38)
	 echo "<option value='38' selected='selected'>38</option>";
	else
	 echo "<option value='38'>38</option>";
	if ($minute == 39)
	 echo "<option value='39' selected='selected'>39</option>";
	else
	 echo "<option value='39'>39</option>";	 
	if ($minute == 40)
	 echo "<option value='40' selected='selected'>40</option>";
	else
	 echo "<option value='40'>40</option>";
	if ($minute == 41)
	 echo "<option value='41' selected='selected'>41</option>";
	else
	 echo "<option value='41'>41</option>";	 
	if ($minute == 42)
	 echo "<option value='42' selected='selected'>42</option>";
	else
	 echo "<option value='42'>42</option>";
	if ($minute == 43)
	 echo "<option value='43' selected='selected'>43</option>";
	else
	 echo "<option value='43'>43</option>";	 
	if ($minute == 44)
	 echo "<option value='44' selected='selected'>44</option>";
	else
	 echo "<option value='44'>44</option>";
	if ($minute == 45)
	 echo "<option value='45' selected='selected'>45</option>";
	else
	 echo "<option value='45'>45</option>";	 
	if ($minute == 46)
	 echo "<option value='46' selected='selected'>46</option>";
	else
	 echo "<option value='46'>46</option>";
	if ($minute == 47)
	 echo "<option value='47' selected='selected'>47</option>";
	else
	 echo "<option value='47'>47</option>";	 
	if ($minute == 48)
	 echo "<option value='48' selected='selected'>48</option>";
	else
	 echo "<option value='48'>48</option>";
	if ($minute == 49)
	 echo "<option value='49' selected='selected'>49</option>";
	else
	 echo "<option value='49'>49</option>";	 
	if ($minute == 50)
	 echo "<option value='50' selected='selected'>50</option>";
	else
	 echo "<option value='50'>50</option>";
	if ($minute == 51)
	 echo "<option value='51' selected='selected'>51</option>";
	else
	 echo "<option value='51'>51</option>";	 
	if ($minute == 52)
	 echo "<option value='52' selected='selected'>52</option>";
	else
	 echo "<option value='52'>52</option>";
	if ($minute == 53)
	 echo "<option value='53' selected='selected'>53</option>";
	else
	 echo "<option value='53'>53</option>";	 
	if ($minute == 54)
	 echo "<option value='54' selected='selected'>54</option>";
	else
	 echo "<option value='54'>54</option>";
	if ($minute == 55)
	 echo "<option value='55' selected='selected'>55</option>";
	else
	 echo "<option value='55'>55</option>";	 
	if ($minute == 56)
	 echo "<option value='56' selected='selected'>56</option>";
	else
	 echo "<option value='56'>56</option>";
	if ($minute == 57)
	 echo "<option value='57' selected='selected'>57</option>";
	else
	 echo "<option value='57'>57</option>";	 
	if ($minute == 58)
	 echo "<option value='58' selected='selected'>58</option>";
	else
	 echo "<option value='58'>58</option>";
	if ($minute == 59)
	 echo "<option value='59' selected='selected'>59</option>";
	else
	 echo "<option value='59'>59</option>";	 	
}	

// Return a list of AM or PM
function getAMPM($ampm) {
	if ($ampm == "am")
	 echo "<option value='am' selected='selected'>am</option>";
	else
	 echo "<option value='am'>am</option>";	
	if ($ampm == "pm")
	 echo "<option value='pm' selected='selected'>pm</option>";
	else
	 echo "<option value='pm'>pm</option>";	 
}

?>