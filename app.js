/* Hearth & Hand — static prototype. Vanilla JS SPA, no dependencies. */
'use strict';

/* ============================== SAMPLE DATA ============================== */
const WORKSHOPS = [
{
  id:'pottery-mug', cat:'Pottery', motif:'pottery',
  title:'Wheel-Thrown Pottery: Your Morning Mug',
  host:'Mud & Meridian Studio', hostSince:'2019', hostBlurb:'A working pottery studio in Union Square run by Priya Nair and Tom Okafor — 14 wheels, one very patient kiln.',
  area:'Somerville', address:'18 Bow St, Union Square, Somerville MA', dist:2.1,
  price:65, dur:'2 hours', rating:4.9, rc:212,
  tags:['beginner-friendly','weekend','date idea'], months:['oct','nov'],
  desc:'Two hours at the wheel, zero experience needed. You\u2019ll center, pull and shape your own stoneware mug, then choose a glaze — we fire it and it\u2019s ready for pickup in two weeks. Aprons, laughs and very forgiving instructors included.',
  included:['All clay, tools and glazes','Firing of 2 finished pieces','Apron + studio towel to use','Chai & biscuits mid-session','Pickup reminder by text'],
  slots:[{d:'Sat, Oct 11',t:'10:00 AM',left:4},{d:'Sun, Oct 12',t:'2:00 PM',left:6},{d:'Sat, Oct 18',t:'10:00 AM',left:2}],
  reviews:[
    {n:'Aisha R.',w:'last weekend',s:5,t:'Made a lopsided mug I am unreasonably proud of. Priya talked me through centering like a meditation. Already booked the bowl class.',m:'pottery'},
    {n:'Marcus T.',w:'2 weeks ago',s:5,t:'Brought my girlfriend for our anniversary — the studio is gorgeous at golden hour and we both left with keepers. Glaze choices are dangerously fun.',m:'pottery2'},
    {n:'Elena V.',w:'last month',s:4,t:'Great intro. Wheels fill fast so book the morning slot if you can — afternoons get warm. My mug survived the kiln!',m:'pottery3'}]
},
{
  id:'pumpkin-carve', cat:'Seasonal', motif:'pumpkin',
  title:'Pumpkin Pick & Carve on the Farm',
  host:'Harvest Hill Farm', hostSince:'2016', hostBlurb:'A family-run farm in Bolton with 40 acres of pumpkins, a cider barn and the friendliest barn cats in Middlesex County.',
  area:'Bolton', address:'140 Harwood Ave, Bolton MA', dist:28,
  price:28, dur:'3 hours', rating:4.8, rc:340,
  tags:['family','outdoors','october'], months:['oct'],
  desc:'Wagon ride out to the patch, pick any pumpkin you can carry, then carve it at long farm tables with hot cider in hand. Stencils for beginners, freehand glory for the brave. Kids under 5 free.',
  included:['Wagon ride to the patch','Any one pumpkin to take home','Carving tools + stencils','Hot apple cider (endless)','Farm animal petting pen'],
  slots:[{d:'Sat, Oct 11',t:'11:00 AM',left:18},{d:'Sun, Oct 12',t:'11:00 AM',left:22},{d:'Sat, Oct 18',t:'11:00 AM',left:9}],
  reviews:[
    {n:'Dave K.',w:'last weekend',s:5,t:'Took the kids and my camera. The wagon ride alone is worth it — mist over the fields, forty acres of orange. Carving tables are well run.',m:'pumpkin'},
    {n:'Sofia M.',w:'2 weeks ago',s:5,t:'Cider is genuinely excellent and they let our toddler "help" carve with the safe tools. We\u2019ll make this a tradition.',m:'pumpkin2'}]
},
{
  id:'pasta', cat:'Food', motif:'pasta',
  title:'Handmade Pasta from Scratch',
  host:'Cucina Elena', hostSince:'2020', hostBlurb:'Elena Rossi\u2019s North End kitchen-school — third-generation pasta maker, first-generation teacher, endlessly patient.',
  area:'North End, Boston', address:'112 Salem St, Boston MA', dist:3.4,
  price:85, dur:'2.5 hours', rating:4.9, rc:186,
  tags:['date idea','dinner included','evening'], months:['oct','nov','dec'],
  desc:'Flour, eggs, and your own two hands. You\u2019ll mix, knead, roll and cut tagliatelle and ravioli, then sit down together to eat everything with Elena\u2019s Sunday rag\u00f9. A glass of Chianti is poured while the dough rests.',
  included:['All ingredients + equipment','Full dinner of what you make','Glass of Chianti (21+)','Printed recipe cards','Dough to take home'],
  slots:[{d:'Fri, Oct 10',t:'6:30 PM',left:5},{d:'Sat, Oct 11',t:'5:00 PM',left:3},{d:'Thu, Oct 16',t:'6:30 PM',left:8}],
  reviews:[
    {n:'James W.',w:'last week',s:5,t:'The ravioli I made looked like little pillows. Elena fixes your technique without making you feel clumsy. Dinner at the end felt like family.',m:'pasta'},
    {n:'Priya S.',w:'3 weeks ago',s:5,t:'Date night upgrade. We laughed the whole time and the recipe cards actually work — made it again at home Sunday.',m:'pasta2'}]
},
{
  id:'sourdough', cat:'Food', motif:'bread',
  title:'Sourdough: From Starter to Loaf',
  host:'Flour & Fern Bakery', hostSince:'2018', hostBlurb:'A Cambridge micro-bakery. Head baker June Park has kept the same starter — "Bubbles" — alive for nine years.',
  area:'Cambridge', address:'7 Inman St, Cambridge MA', dist:4.2,
  price:55, dur:'2 hours', rating:4.7, rc:158,
  tags:['beginner-friendly','take home'], months:['oct','nov'],
  desc:'Demystify sourdough in one morning: feed a starter, mix and fold your dough, score like a pro, and leave with a proofing loaf plus a jar of Bubbles\u2019 offspring to start your own lineage.',
  included:['Live starter culture to keep','Banneton basket (yours)','Fresh-baked tastings','Baker\u2019s timeline fridge card','Flour sack tote'],
  slots:[{d:'Sun, Oct 12',t:'9:30 AM',left:7},{d:'Sat, Oct 18',t:'9:30 AM',left:4}],
  reviews:[
    {n:'Hannah L.',w:'last weekend',s:5,t:'My starter "Dumpling" is thriving two weeks later. June answers every anxious text like a sourdough doula.',m:'bread'},
    {n:'Rob C.',w:'last month',s:4,t:'Dense with info — take notes. The tasting flight of three loaves at the end is a masterclass by itself.',m:'bread2'}]
},
{
  id:'knit-scarf', cat:'Fiber', motif:'knit',
  title:'Knit Your First Scarf',
  host:'Purl & Loop', hostSince:'2021', hostBlurb:'Jamaica Plain\u2019s cozy yarn shop-caf\u00e9. Owner Bea Fontaine teaches like your favorite aunt — if your aunt had strong opinions about wool.',
  area:'Jamaica Plain', address:'412 Centre St, Jamaica Plain MA', dist:5.1,
  price:45, dur:'2 hours', rating:4.9, rc:203,
  tags:['beginner-friendly','cozy','evening'], months:['oct','nov','dec'],
  desc:'Cast on, knit, purl, cast off — by the end of the evening you\u2019ll have a real scarf started and the skills to finish it on the couch. Yarn, needles and endless tea are on us.',
  included:['Skein of soft wool yarn','Bamboo needles (yours to keep)','Printed stitch guide','Tea, cocoa & cookies','10% off shop yarn after'],
  slots:[{d:'Wed, Oct 15',t:'6:30 PM',left:6},{d:'Sat, Oct 18',t:'1:00 PM',left:9},{d:'Wed, Oct 22',t:'6:30 PM',left:6}],
  reviews:[
    {n:'Grace N.',w:'last week',s:5,t:'I\u2019d never held needles. Bea had me knitting actual rows in 20 minutes. The shop smells like wool and vanilla — I never wanted to leave.',m:'knit'},
    {n:'Tom H.',w:'2 weeks ago',s:5,t:'Bought this for my wife, stayed for myself. Finished my scarf during the Pats game. 10/10 would purl again.',m:'knit2'}]
},
{
  id:'watercolor', cat:'Art', motif:'paint',
  title:'Watercolor: Autumn Landscapes',
  host:'Atelier Blick', hostSince:'2017', hostBlurb:'Arlington studio of painter Dana Whitfield, whose New England autumns hang in three Boston galleries.',
  area:'Arlington', address:'22 Medford St, Arlington MA', dist:6.8,
  price:60, dur:'2 hours', rating:4.8, rc:97,
  tags:['beginner-friendly','weekend'], months:['oct'],
  desc:'Paint the season while it\u2019s happening. Dana demos washes, wet-on-wet skies and one-stroke trees, then you paint your own 8\u00d710 autumn scene to frame. All pro-grade paints provided.',
  included:['Professional paint set to use','Cold-press paper (3 sheets)','Brushes + palette','Frame for your best piece','Cider + cheese board'],
  slots:[{d:'Sun, Oct 12',t:'1:00 PM',left:5},{d:'Sat, Oct 18',t:'1:00 PM',left:8}],
  reviews:[
    {n:'Maya D.',w:'last weekend',s:5,t:'My painting actually looks like fall. Dana\u2019s one-stroke tree trick felt like magic. Framed mine the same night.',m:'paint'},
    {n:'Chris B.',w:'last month',s:5,t:'Zero art background, left with something I\u2019m hanging in my office. Small class, lots of 1:1 help.',m:'paint2'}]
},
{
  id:'croissant', cat:'Food', motif:'croissant',
  title:'Croissants: Laminate Like Paris',
  host:'Butter Theory', hostSince:'2022', hostBlurb:'South End laminated-dough lab run by ex-Parisian baker Camille Fournier. The smell hits you a block away.',
  area:'South End, Boston', address:'88 Waltham St, Boston MA', dist:3.9,
  price:95, dur:'3 hours', rating:5.0, rc:88,
  tags:['advanced','weekend'], months:['nov','dec'],
  desc:'The full lamination: detrempe, beurrage, three folds, shaping. You\u2019ll proof and bake a batch in class and take dough home to bake fresh Sunday morning. Flaky layers guaranteed or Camille eats her toque.',
  included:['All ingredients (French butter!)','Dough to bake at home','Baker\u2019s lame + scraper','Fresh croissants to eat','Lamination schedule card'],
  slots:[{d:'Sat, Oct 18',t:'9:00 AM',left:4},{d:'Sun, Oct 26',t:'9:00 AM',left:6}],
  reviews:[
    {n:'Olivia P.',w:'2 weeks ago',s:5,t:'Twenty-seven layers. TWENTY-SEVEN. I counted. Camille is a genius and a comedian. Worth every penny.',m:'croissant'},
    {n:'Sam R.',w:'last month',s:5,t:'Hardest I\u2019ve concentrated in years and the most delicious. My home batch Sunday made my roommates weep.',m:'croissant2'}]
},
{
  id:'salsa', cat:'Dance', motif:'dance',
  title:'Beginner Salsa Night',
  host:'Ritmo Dance Co.', hostSince:'2019', hostBlurb:'East Boston\u2019s friendliest dance floor. Instructors Marco & Lucia have taught 3,000+ two-left-footed Bostonians.',
  area:'East Boston', address:'310 Meridian St, East Boston MA', dist:4.5,
  price:30, dur:'1.5 hours', rating:4.7, rc:141,
  tags:['date idea','evening','no partner needed'], months:['oct','nov','dec'],
  desc:'No partner, no experience, no problem. 90 minutes of basic steps, turns and one very satisfying dip — then open social dancing with the lights low and the playlist high.',
  included:['90-min guided lesson','Open social dance after','Partner rotation (optional)','Shoe & floor tips','First-drink token at the bar'],
  slots:[{d:'Fri, Oct 10',t:'8:00 PM',left:12},{d:'Fri, Oct 17',t:'8:00 PM',left:14}],
  reviews:[
    {n:'Nina F.',w:'last week',s:5,t:'Walked in terrified, danced out glowing. The rotation means you meet everyone. My Friday nights are booked now.',m:'dance'},
    {n:'Alex G.',w:'3 weeks ago',s:4,t:'Great energy, loud in the best way. Wear shoes that slide — learned that the fun way.',m:'dance2'}]
},
{
  id:'fall-garden', cat:'Garden', motif:'garden',
  title:'Fall Garden: Bulbs & Beds',
  host:'Green Thumb Collective', hostSince:'2015', hostBlurb:'Brookline community gardeners who believe October is the real start of the gardening year.',
  area:'Brookline', address:'45 Netherlands Rd, Brookline MA', dist:5.6,
  price:40, dur:'2 hours', rating:4.8, rc:76,
  tags:['outdoors','october','take home'], months:['oct'],
  desc:'Put the garden to bed and plant next spring\u2019s show. You\u2019ll plant 25 bulbs in the teaching beds, learn lasagna layering, and take home a bulb collection for your own plot or pots.',
  included:['25-bulb collection to plant','All tools + gloves','Soil & compost','Hot mulled cider','Planting map for spring'],
  slots:[{d:'Sat, Oct 11',t:'10:00 AM',left:10},{d:'Sun, Oct 19',t:'10:00 AM',left:12}],
  reviews:[
    {n:'Karen J.',w:'last weekend',s:5,t:'I finally understand what I\u2019m doing out there in October. My pots are planted and I\u2019m weirdly excited for April.',m:'garden'},
    {n:'Dev P.',w:'2 weeks ago',s:4,t:'Hands in soil for two hours = best therapy. Take the cider they offer at the start, not the end.',m:'garden2'}]
},
{
  id:'truffles', cat:'Food', motif:'chocolate',
  title:'Chocolate Truffles & Bonbons',
  host:'Cacao Craft', hostSince:'2020', hostBlurb:'Beacon Hill bean-to-bar studio. Chocolatier Amara Diallo trained in Brussels and temper-tests everything twice.',
  area:'Beacon Hill', address:'14 Charles St, Boston MA', dist:3.1,
  price:75, dur:'2 hours', rating:4.9, rc:112,
  tags:['date idea','take home','evening'], months:['nov','dec'],
  desc:'Ganache, roll, dip, decorate. You\u2019ll make 24 truffles in dark, milk and white — spiked, spiced and classic — and box them like a pro. Tempering demo included (it\u2019s basically chocolate science).',
  included:['24 handmade truffles','Gift box + ribbon','All tools & couverture','Tempering demo','Tasting flight of 6'],
  slots:[{d:'Sat, Oct 11',t:'3:00 PM',left:6},{d:'Fri, Oct 17',t:'6:30 PM',left:8}],
  reviews:[
    {n:'Rachel S.',w:'last weekend',s:5,t:'My gift boxes looked STORE-BOUGHT. Amara\u2019s tempering demo finally made it click. Ate four on the T home.',m:'choc'},
    {n:'Ben O.',w:'last month',s:5,t:'Date night champion. We fought over the salted caramel ones. Book the Friday slot — smaller class.',m:'choc2'}]
},
{
  id:'terrarium', cat:'Garden', motif:'terrarium',
  title:'Build a Living Terrarium',
  host:'Moss & Stone', hostSince:'2021', hostBlurb:'Somerville plant studio specializing in tiny ecosystems. Owner Wren Ito can name every moss.',
  area:'Somerville', address:'92 Highland Ave, Somerville MA', dist:2.4,
  price:50, dur:'1.5 hours', rating:4.8, rc:129,
  tags:['beginner-friendly','take home','weekend'], months:['oct','nov'],
  desc:'Layer stone, soil, moss and three tiny plants into a glass world that (mostly) takes care of itself. Wren covers the science of the water cycle in a jar and how not to kill it.',
  included:['Glass vessel (yours)','3 live plants + moss','Stones, soil, charcoal','Care card','Mister bottle'],
  slots:[{d:'Sun, Oct 12',t:'11:00 AM',left:8},{d:'Sat, Oct 18',t:'3:00 PM',left:5}],
  reviews:[
    {n:'Jenny L.',w:'last weekend',s:5,t:'Mine sits on my desk and coworkers keep asking about it. Wren\u2019s care tips are gold — mine\u2019s still alive!',m:'terra'},
    {n:'Omar H.',w:'3 weeks ago',s:4,t:'Relaxing and weirdly fascinating. Go easy on the water — trust the mister, not your instincts.',m:'terra2'}]
},
{
  id:'pottery-intensive', cat:'Pottery', motif:'pottery2',
  title:'Two-Day Pottery Intensive',
  host:'Mud & Meridian Studio', hostSince:'2019', hostBlurb:'A working pottery studio in Union Square run by Priya Nair and Tom Okafor — 14 wheels, one very patient kiln.',
  area:'Somerville', address:'18 Bow St, Union Square, Somerville MA', dist:2.1,
  price:180, dur:'2 days · 12 hours', rating:5.0, rc:64,
  tags:['weekend','deep dive'], months:['nov'],
  desc:'A full weekend at the wheel: throwing, trimming, handles, glazing theory and your own shelf in the kiln room. Leave with 6–8 finished pieces and genuinely solid fundamentals. Lunch both days included.',
  included:['12 hours of instruction','All clay, glazes & firings','6–8 finished pieces','Lunch both days','Studio apron (yours)','Open-studio pass for a month'],
  slots:[{d:'Sat–Sun, Nov 8–9',t:'10:00 AM – 4:00 PM',left:3},{d:'Sat–Sun, Nov 22–23',t:'10:00 AM – 4:00 PM',left:5}],
  reviews:[
    {n:'Diana K.',w:'last month',s:5,t:'Went in never having touched clay, came out with bowls I actually use daily. The month of open studio after is the secret best part.',m:'pottery'},
    {n:'Leo M.',w:'2 months ago',s:5,t:'Intense in the best way. By Sunday afternoon I was trimming like I\u2019d done it for years. Hands were wrecked, heart was full.',m:'pottery2'}]
},
{
  id:'wreath', cat:'Seasonal', motif:'wreath',
  title:'Winter Wreath: Fresh Greens',
  host:'Green Thumb Collective', hostSince:'2015', hostBlurb:'Brookline community gardeners who believe October is the real start of the gardening year.',
  area:'Brookline', address:'45 Netherlands Rd, Brookline MA', dist:5.6,
  price:55, dur:'2 hours', rating:4.9, rc:58,
  tags:['december','take home','cozy'], months:['dec'],
  desc:'Build a full, fragrant wreath from fresh-cut fir, cedar and pine — wired, not glued, so it lasts till January. Dried oranges, cinnamon and ribbon bar included. Mulled wine for the 21+.',
  included:['18" wreath frame (yours)','Fresh greens bundle','Dried fruit + ribbon bar','Wiring tools','Mulled wine / cider'],
  slots:[{d:'Sat, Dec 6',t:'1:00 PM',left:10},{d:'Sun, Dec 7',t:'1:00 PM',left:12}],
  reviews:[
    {n:'Carol W.',w:'last December',s:5,t:'My front door has never looked this good. The fresh-greens smell filled my car for days. Doing it again this year.',m:'wreath'},
    {n:'Anita G.',w:'last December',s:5,t:'Wired-not-glued is the difference — mine lasted 7 weeks. The ribbon bar is dangerously fun.',m:'wreath2'}]
}
];

const FEED = [
 {w:'pottery-mug', n:'Aisha R.', when:'2 days ago', s:5, likes:48, m:'pottery',
  t:'My mug survived the kiln!! Slightly lopsided, completely perfect. Already planning which glaze for the bowl class.', tag:'Pottery'},
 {w:'pumpkin-carve', n:'Dave K.', when:'3 days ago', s:5, likes:112, m:'pumpkin',
  t:'Forty acres of pumpkins and a wagon ride through morning mist. The kids are still talking about the barn cats.', tag:'Seasonal'},
 {w:'pasta', n:'Priya S.', when:'5 days ago', s:5, likes:67, m:'pasta',
  t:'Date night at Cucina Elena — we made ravioli that looked like little pillows and ate dinner together after. Recipe cards actually work at home!', tag:'Food'},
 {w:'knit-scarf', n:'Tom H.', when:'1 week ago', s:5, likes:39, m:'knit',
  t:'Bought the scarf class for my wife, stayed for myself. Finished it during the game. 10/10 would purl again.', tag:'Fiber'},
 {w:'salsa', n:'Nina F.', when:'1 week ago', s:5, likes:54, m:'dance',
  t:'Walked in terrified of dancing, walked out glowing. The partner rotation means you meet everyone. Friday nights = booked.', tag:'Dance'},
 {w:'terrarium', n:'Jenny L.', when:'2 weeks ago', s:5, likes:41, m:'terra',
  t:'My little glass world is thriving on my desk. Wren\u2019s care tips are gold — three weeks and everything\u2019s still alive!', tag:'Garden'}
];

const SEASONS = {
 oct:{name:'October', theme:'Harvest Season', sub:'Pumpkins, apples, autumn color — the land is showing off.', motif:'pumpkin'},
 nov:{name:'November', theme:'Hearth & Craft', sub:'Cozy indoor making: clay, wool, chocolate and lamination.', motif:'knit'},
 dec:{name:'December', theme:'Winter Magic', sub:'Wreaths, truffles and gifts made by your own hands.', motif:'wreath'}
};

const CATS = ['All','Pottery','Food','Fiber','Art','Garden','Dance','Seasonal'];

/* ============================== SVG SCENES ==============================
   Hand-crafted layered scenes (risograph-print style). No external images. */
function scene(key, label){
  const S = {
    pottery:  {sky:['#F3D9B8','#D9A06B'], sun:'#FFF3DC', hills:['#C98A52','#A9683A'],
      draw:`<g><circle cx="400" cy="150" r="58" fill="#FFF3DC" opacity=".9"/><circle cx="400" cy="150" r="86" fill="#FFF3DC" opacity=".25"/><path d="M0 430 Q200 380 400 420 T800 410 V600 H0 Z" fill="#C98A52"/><path d="M0 480 Q260 440 520 470 T800 460 V600 H0 Z" fill="#A9683A"/><g fill="none" stroke="#8A4E26" stroke-width="5" opacity=".55"><ellipse cx="400" cy="520" rx="150" ry="26"/><ellipse cx="400" cy="520" rx="105" ry="18"/><ellipse cx="400" cy="520" rx="62" ry="11"/></g><path d="M368 380 q-6-60 10-96 q4-10 14-10 h16 q10 0 14 10 q16 36 10 96 q-30 14 -64 0 Z" fill="#7A3F1F"/><ellipse cx="400" cy="382" rx="34" ry="9" fill="#5E2F16"/></g>`},
    pottery2: {sky:['#EFD9C2','#C99A6E'], sun:'#FFF6E2', hills:['#B57E4C','#8F5C33'],
      draw:`<g><circle cx="400" cy="140" r="52" fill="#FFF6E2" opacity=".9"/><path d="M0 440 Q220 390 430 425 T800 415 V600 H0 Z" fill="#B57E4C"/><path d="M0 490 Q280 450 540 478 T800 470 V600 H0 Z" fill="#8F5C33"/><g fill="#7A3F1F"><path d="M300 420 q-8-70 12-110 q5-12 17-12 h10 q12 0 17 12 q20 40 12 110 q-34 16 -68 0 Z"/><path d="M452 430 q-6-52 9-82 q4-9 13-9 h8 q9 0 13 9 q15 30 9 82 q-26 12 -52 0 Z"/></g><g fill="none" stroke="#6E3418" stroke-width="4" opacity=".5"><ellipse cx="334" cy="560" rx="120" ry="20"/><ellipse cx="478" cy="565" rx="90" ry="16"/></g></g>`},
    pottery3: {sky:['#F6E3C6','#DFAF7E'], sun:'#FFF8E6', hills:['#C9905C','#9C6538'],
      draw:`<g><circle cx="400" cy="150" r="55" fill="#FFF8E6" opacity=".9"/><path d="M0 435 Q210 385 410 420 T800 410 V600 H0 Z" fill="#C9905C"/><path d="M0 485 Q270 445 530 472 T800 465 V600 H0 Z" fill="#9C6538"/><path d="M372 400 q-5-55 8-88 q4-9 12-9 h16 q8 0 12 9 q13 33 8 88 q-28 12 -56 0 Z" fill="#7A3F1F"/><g fill="#5E2F16" opacity=".8"><circle cx="330" cy="330" r="10"/><circle cx="470" cy="320" r="8"/><circle cx="410" cy="290" r="6"/></g></g>`},
    pumpkin:  {sky:['#F2C078','#D98A4B'], sun:'#FFF0D0', hills:['#B96A35','#8F4E26'],
      draw:`<g><circle cx="620" cy="130" r="60" fill="#FFF0D0" opacity=".9"/><path d="M0 420 Q230 370 450 410 T800 400 V600 H0 Z" fill="#B96A35"/><path d="M0 480 Q280 435 540 465 T800 455 V600 H0 Z" fill="#8F4E26"/><g><ellipse cx="280" cy="480" rx="70" ry="58" fill="#C2571F"/><ellipse cx="400" cy="500" rx="88" ry="70" fill="#D2691E"/><ellipse cx="530" cy="485" rx="62" ry="52" fill="#B54E1A"/><g stroke="#7A3410" stroke-width="4" fill="none" opacity=".7"><path d="M280 425 q-8 55 0 110 M280 425 q8 55 0 110 M400 432 q-10 68 0 136 M400 432 q10 68 0 136 M530 435 q-7 50 0 100 M530 435 q7 50 0 100"/></g><g stroke="#4E6B2F" stroke-width="7" fill="none"><path d="M400 432 q30-40 70-45 q-25 20 -18 42"/></g><rect x="392" y="400" width="16" height="34" rx="6" fill="#6B4A1F"/></g></g>`},
    pumpkin2: {sky:['#EFB96F','#CE8040'], sun:'#FFEFD2', hills:['#A9602F','#7E4423'],
      draw:`<g><circle cx="200" cy="120" r="55" fill="#FFEFD2" opacity=".9"/><path d="M0 430 Q240 380 460 415 T800 405 V600 H0 Z" fill="#A9602F"/><path d="M0 485 Q290 440 550 470 T800 460 V600 H0 Z" fill="#7E4423"/><g><ellipse cx="400" cy="495" rx="95" ry="75" fill="#C2571F"/><g stroke="#7A3410" stroke-width="4" fill="none" opacity=".7"><path d="M400 422 q-10 73 0 146 M400 422 q12 73 4 146 M400 422 q-24 70 -18 140 M400 422 q24 70 20 140"/></g><rect x="392" y="392" width="16" height="34" rx="6" fill="#6B4A1F"/><path d="M400 396 q-40-30 -70-28" stroke="#4E6B2F" stroke-width="7" fill="none"/></g><circle cx="560" cy="540" r="26" fill="#D2691E" opacity=".9"/><circle cx="240" cy="545" r="20" fill="#B54E1A" opacity=".9"/></g>`},
    pasta:    {sky:['#F2DEAC','#DDBB6E'], sun:'#FFF8DE', hills:['#C9A24E','#A07E38'],
      draw:`<g><circle cx="400" cy="130" r="62" fill="#FFF8DE" opacity=".95"/><path d="M0 430 Q230 380 450 415 T800 405 V600 H0 Z" fill="#C9A24E"/><path d="M0 485 Q290 440 550 468 T800 460 V600 H0 Z" fill="#A07E38"/><g stroke="#7A5E22" stroke-width="5" fill="none" opacity=".85"><path d="M250 560 L250 380 M250 420 q-30-8 -44-30 M250 420 q30-8 44-30 M250 470 q-30-8 -44-30 M250 470 q30-8 44-30 M550 570 L550 390 M550 430 q-30-8 -44-30 M550 430 q30-8 44-30 M550 480 q-30-8 -44-30 M550 480 q30-8 44-30"/></g><g fill="#E8C96A"><ellipse cx="206" cy="392" rx="10" ry="20" transform="rotate(-30 206 392)"/><ellipse cx="294" cy="392" rx="10" ry="20" transform="rotate(30 294 392)"/><ellipse cx="506" cy="402" rx="10" ry="20" transform="rotate(-30 506 402)"/><ellipse cx="594" cy="402" rx="10" ry="20" transform="rotate(30 594 402)"/></g></g>`},
    pasta2:   {sky:['#F5E4B8','#E2C078'], sun:'#FFFBEB', hills:['#CBA855','#9E7C36'],
      draw:`<g><circle cx="620" cy="140" r="55" fill="#FFFBEB" opacity=".9"/><path d="M0 440 Q220 390 440 420 T800 412 V600 H0 Z" fill="#CBA855"/><path d="M0 490 Q280 448 540 475 T800 466 V600 H0 Z" fill="#9E7C36"/><g fill="none" stroke="#8A6A2A" stroke-width="14" opacity=".55"><path d="M180 480 q60-26 120 0 t120 0 t120 0"/></g><g fill="none" stroke="#B98F3A" stroke-width="10"><path d="M200 520 q50-22 100 0 t100 0 t100 0"/></g></g>`},
    bread:    {sky:['#F0D3A0','#D5A05E'], sun:'#FFF2D8', hills:['#B57E44','#8A5A30'],
      draw:`<g><circle cx="400" cy="135" r="58" fill="#FFF2D8" opacity=".9"/><path d="M0 435 Q220 385 440 418 T800 408 V600 H0 Z" fill="#B57E44"/><path d="M0 488 Q280 445 540 472 T800 464 V600 H0 Z" fill="#8A5A30"/><ellipse cx="400" cy="490" rx="150" ry="62" fill="#C98A3E"/><ellipse cx="400" cy="472" rx="150" ry="48" fill="#DE9F52"/><g stroke="#8A5220" stroke-width="7" fill="none" opacity=".8"><path d="M320 455 l36 26 M380 448 l36 26 M440 448 l36 26"/></g><g fill="#FFF2D8" opacity=".35"><circle cx="250" cy="300" r="8"/><circle cx="560" cy="280" r="6"/><circle cx="480" cy="340" r="5"/></g></g>`},
    bread2:   {sky:['#EDCBA0','#D09A58'], sun:'#FFF4DC', hills:['#AC7640','#82552C'],
      draw:`<g><circle cx="180" cy="130" r="52" fill="#FFF4DC" opacity=".9"/><path d="M0 440 Q230 390 450 420 T800 410 V600 H0 Z" fill="#AC7640"/><path d="M0 490 Q290 448 550 474 T800 466 V600 H0 Z" fill="#82552C"/><g><ellipse cx="330" cy="495" rx="90" ry="46" fill="#C98A3E"/><ellipse cx="330" cy="482" rx="90" ry="34" fill="#DE9F52"/><ellipse cx="520" cy="505" rx="66" ry="34" fill="#B57A34"/><ellipse cx="520" cy="495" rx="66" ry="25" fill="#D0934A"/></g></g>`},
    knit:     {sky:['#EAD9CE','#CFAE9C'], sun:'#FFF6EC', hills:['#B08D76','#86664F'],
      draw:`<g><circle cx="400" cy="140" r="55" fill="#FFF6EC" opacity=".9"/><path d="M0 435 Q220 388 440 420 T800 410 V600 H0 Z" fill="#B08D76"/><path d="M0 488 Q280 446 540 472 T800 464 V600 H0 Z" fill="#86664F"/><circle cx="400" cy="470" r="95" fill="#A8512F"/><g stroke="#7E3A1E" stroke-width="5" fill="none" opacity=".75"><path d="M312 450 q88 -30 176 0 M310 475 q90 -32 180 0 M314 500 q86 -30 172 0 M322 524 q78 -26 156 0"/></g><g stroke="#5E4632" stroke-width="9"><path d="M495 430 L620 300 M520 445 L645 315"/></g><circle cx="628" cy="292" r="13" fill="#5E4632"/><circle cx="653" cy="307" r="13" fill="#5E4632"/><path d="M495 520 q120 30 210 -10" stroke="#A8512F" stroke-width="12" fill="none"/></g>`},
    knit2:    {sky:['#E5D5C6','#C6A892'], sun:'#FFF8F0', hills:['#A98870','#7E5F48'],
      draw:`<g><circle cx="620" cy="130" r="52" fill="#FFF8F0" opacity=".9"/><path d="M0 440 Q230 392 450 422 T800 412 V600 H0 Z" fill="#A98870"/><path d="M0 492 Q290 450 550 476 T800 468 V600 H0 Z" fill="#7E5F48"/><g fill="none" stroke="#6E7D5B" stroke-width="16" opacity=".9"><path d="M220 560 q40-120 90-160 q60-48 130-40"/></g><g fill="none" stroke="#8A9A72" stroke-width="16" opacity=".9"><path d="M280 570 q30-100 80-135"/></g><circle cx="440" cy="395" r="16" fill="#4E5F3A"/></g>`},
    paint:    {sky:['#CBDCE8','#9DB8CC'], sun:'#F4FAFF', hills:['#7E9AAE','#5B7689'],
      draw:`<g><circle cx="400" cy="130" r="55" fill="#F4FAFF" opacity=".95"/><path d="M0 430 Q230 380 450 412 T800 402 V600 H0 Z" fill="#7E9AAE"/><path d="M0 485 Q290 442 550 468 T800 460 V600 H0 Z" fill="#5B7689"/><g opacity=".9"><ellipse cx="300" cy="300" rx="90" ry="46" fill="#D98A6B" transform="rotate(-14 300 300)"/><ellipse cx="500" cy="330" rx="100" ry="50" fill="#C96A5A" transform="rotate(10 500 330)"/><ellipse cx="400" cy="380" rx="110" ry="42" fill="#E0A35C" transform="rotate(-6 400 380)"/></g><g><rect x="560" y="440" width="26" height="90" rx="10" fill="#6B4A2A" transform="rotate(24 573 485)"/><path d="M585 440 q40-30 90-24" stroke="#C96A5A" stroke-width="22" fill="none" transform="rotate(24 573 485)"/></g></g>`},
    paint2:   {sky:['#D8E2EC','#A9BED2'], sun:'#F6FBFF', hills:['#8AA0B2','#64798A'],
      draw:`<g><circle cx="200" cy="130" r="50" fill="#F6FBFF" opacity=".95"/><path d="M0 435 Q230 385 450 415 T800 405 V600 H0 Z" fill="#8AA0B2"/><path d="M0 488 Q290 445 550 470 T800 462 V600 H0 Z" fill="#64798A"/><circle cx="420" cy="430" r="80" fill="#E8DCC2"/><circle cx="395" cy="415" r="14" fill="#C96A5A"/><circle cx="440" cy="410" r="14" fill="#6E7D5B"/><circle cx="445" cy="448" r="14" fill="#5F7F92"/><circle cx="398" cy="450" r="14" fill="#D9A13B"/><path d="M500 430 q60-10 90-50" stroke="#6B4A2A" stroke-width="16" fill="none"/></g>`},
    croissant:{sky:['#F4DFAE','#E0B96E'], sun:'#FFFAE6', hills:['#C69A4E','#9C7434'],
      draw:`<g><circle cx="400" cy="130" r="60" fill="#FFFAE6" opacity=".95"/><path d="M0 435 Q220 385 440 418 T800 408 V600 H0 Z" fill="#C69A4E"/><path d="M0 488 Q280 445 540 472 T800 464 V600 H0 Z" fill="#9C7434"/><g><path d="M250 470 q150 -80 300 0 q-40 60 -90 70 q-30 -50 -60 -50 q-30 0 -60 50 q-50 -10 -90 -70 Z" fill="#D99A3E"/><path d="M285 462 q115 -58 230 0 q-28 42 -64 50 q-22 -36 -51 -36 q-29 0 -51 36 q-36 -8 -64 -50 Z" fill="#E8AE54"/><g stroke="#A86E22" stroke-width="5" fill="none" opacity=".7"><path d="M340 440 q60 -22 120 0 M320 470 q80 -28 160 0"/></g></g></g>`},
    croissant2:{sky:['#F2D8A4','#DBB268'], sun:'#FFFBEA', hills:['#BE9248','#96702F'],
      draw:`<g><circle cx="620" cy="135" r="55" fill="#FFFBEA" opacity=".9"/><path d="M0 440 Q230 390 450 420 T800 410 V600 H0 Z" fill="#BE9248"/><path d="M0 492 Q290 448 550 474 T800 466 V600 H0 Z" fill="#96702F"/><g opacity=".95"><path d="M180 500 q110 -60 220 0 q-30 44 -66 52 q-22 -36 -44 -36 q-22 0 -44 36 q-36 -8 -66 -52 Z" fill="#D99A3E"/><path d="M470 520 q90 -48 180 0 q-24 36 -54 42 q-18 -30 -36 -30 q-18 0 -36 30 q-30 -6 -54 -42 Z" fill="#E0A64E"/></g></g>`},
    dance:    {sky:['#4A2E45','#2E1B2C'], sun:'#F2D8A0', hills:['#3A2336','#241722'],
      draw:`<g><circle cx="620" cy="120" r="52" fill="#F2D8A0" opacity=".9"/><circle cx="620" cy="120" r="80" fill="#F2D8A0" opacity=".18"/><path d="M0 450 Q240 400 470 432 T800 422 V600 H0 Z" fill="#3A2336"/><path d="M0 505 Q290 465 560 490 T800 482 V600 H0 Z" fill="#241722"/><g fill="#E8B64C"><circle cx="330" cy="300" r="26"/><path d="M330 330 q-50 40 -70 110 l36 10 q14 -60 44 -88 q30 28 44 88 l36 -10 q-20 -70 -70 -110 Z"/><circle cx="480" cy="280" r="24"/><path d="M480 308 q10 60 -30 100 q60 10 80 -40 q30 30 20 90 l34 -6 q6 -70 -34 -110 q-40 -30 -70 -34 Z"/></g><g fill="#F2D8A0" opacity=".5"><circle cx="150" cy="180" r="4"/><circle cx="700" cy="220" r="5"/><circle cx="520" cy="140" r="4"/><circle cx="260" cy="110" r="3"/></g></g>`},
    dance2:   {sky:['#5E3A52','#38222F'], sun:'#F6DFAE', hills:['#462A3E','#2B1A26'],
      draw:`<g><circle cx="200" cy="120" r="48" fill="#F6DFAE" opacity=".9"/><path d="M0 450 Q240 402 470 432 T800 424 V600 H0 Z" fill="#462A3E"/><path d="M0 505 Q290 466 560 490 T800 484 V600 H0 Z" fill="#2B1A26"/><g fill="#D9A13B"><path d="M400 250 q90 30 110 120 q-60 20 -110 -10 q-50 30 -110 10 q20 -90 110 -120 Z"/><circle cx="400" cy="225" r="22"/></g><g stroke="#F6DFAE" stroke-width="6" fill="none" opacity=".6"><path d="M290 330 q110 60 220 0"/></g></g>`},
    garden:   {sky:['#DCE8D2','#A9C294'], sun:'#F6FBEF', hills:['#7E9A66','#5B7347'],
      draw:`<g><circle cx="400" cy="130" r="58" fill="#F6FBEF" opacity=".95"/><path d="M0 430 Q230 380 450 412 T800 402 V600 H0 Z" fill="#7E9A66"/><path d="M0 485 Q290 442 550 468 T800 460 V600 H0 Z" fill="#5B7347"/><g><path d="M400 520 L400 400" stroke="#4E6B2F" stroke-width="10"/><g fill="#6E9A4E"><ellipse cx="360" cy="450" rx="42" ry="20" transform="rotate(-28 360 450)"/><ellipse cx="440" cy="450" rx="42" ry="20" transform="rotate(28 440 450)"/><ellipse cx="400" cy="410" rx="36" ry="18" transform="rotate(-8 400 410)"/></g><circle cx="400" cy="385" r="20" fill="#D98A6B"/></g><g fill="#8A9A72" opacity=".8"><ellipse cx="220" cy="540" rx="26" ry="10"/><ellipse cx="580" cy="545" rx="30" ry="11"/></g></g>`},
    garden2:  {sky:['#D2E0C6','#9DBB88'], sun:'#F8FDEF', hills:['#75905E','#54683F'],
      draw:`<g><circle cx="180" cy="130" r="52" fill="#F8FDEF" opacity=".9"/><path d="M0 435 Q230 385 450 415 T800 405 V600 H0 Z" fill="#75905E"/><path d="M0 488 Q290 445 550 470 T800 462 V600 H0 Z" fill="#54683F"/><g stroke="#4E6B2F" stroke-width="8" fill="none"><path d="M250 560 L250 440 M550 560 L550 450"/></g><g fill="#6E9A4E"><ellipse cx="250" cy="415" rx="48" ry="26"/><ellipse cx="550" cy="425" rx="42" ry="24"/></g><g fill="#C96A5A"><circle cx="230" cy="405" r="10"/><circle cx="262" cy="420" r="9"/><circle cx="548" cy="416" r="9"/></g></g>`},
    chocolate:{sky:['#6B4A33','#3E2A1D'], sun:'#F2D8A0', hills:['#4E3524','#2E1F15'],
      draw:`<g><circle cx="400" cy="120" r="50" fill="#F2D8A0" opacity=".85"/><path d="M0 450 Q240 405 470 435 T800 425 V600 H0 Z" fill="#4E3524"/><path d="M0 505 Q290 468 560 492 T800 484 V600 H0 Z" fill="#2E1F15"/><g><circle cx="300" cy="480" r="46" fill="#5E3A22"/><circle cx="420" cy="500" r="56" fill="#6E4429"/><circle cx="540" cy="480" r="42" fill="#54331E"/><g stroke="#D9A96B" stroke-width="5" fill="none" opacity=".85"><path d="M262 470 q38 -18 76 0 M374 488 q46 -22 92 0 M506 470 q34 -16 68 0"/></g><g fill="#F2D8A0" opacity=".9"><circle cx="300" cy="466" r="6"/><circle cx="420" cy="482" r="7"/><circle cx="540" cy="466" r="6"/></g></g></g>`},
    choc:     {sky:['#7A563C','#46301F'], sun:'#F6DDAE', hills:['#553A26','#332216'],
      draw:`<g><circle cx="620" cy="120" r="48" fill="#F6DDAE" opacity=".85"/><path d="M0 450 Q240 405 470 435 T800 425 V600 H0 Z" fill="#553A26"/><path d="M0 505 Q290 468 560 492 T800 484 V600 H0 Z" fill="#332216"/><g><rect x="300" y="440" width="200" height="90" rx="18" fill="#5E3A22"/><g fill="#7A4E2C"><circle cx="345" cy="485" r="24"/><circle cx="410" cy="485" r="24"/><circle cx="475" cy="485" r="24"/></g><g stroke="#F2D8A0" stroke-width="4" fill="none" opacity=".8"><path d="M330 478 q15 -10 30 0 M395 478 q15 -10 30 0 M460 478 q15 -10 30 0"/></g></g></g>`},
    choc2:    {sky:['#6B4A33','#3E2A1D'], sun:'#F2D8A0', hills:['#4E3524','#2E1F15'],
      draw:`<g><circle cx="180" cy="120" r="46" fill="#F2D8A0" opacity=".85"/><path d="M0 450 Q240 405 470 435 T800 425 V600 H0 Z" fill="#4E3524"/><path d="M0 505 Q290 468 560 492 T800 484 V600 H0 Z" fill="#2E1F15"/><g fill="#6E4429"><ellipse cx="400" cy="490" rx="130" ry="44"/></g><g fill="#8A5A34"><circle cx="340" cy="480" r="26"/><circle cx="410" cy="492" r="26"/><circle cx="478" cy="480" r="26"/></g></g>`},
    terrarium:{sky:['#CFE0DC','#9DBFB6'], sun:'#F2FAF7', hills:['#7E9A92','#5B726B'],
      draw:`<g><circle cx="400" cy="130" r="55" fill="#F2FAF7" opacity=".95"/><path d="M0 435 Q230 385 450 415 T800 405 V600 H0 Z" fill="#7E9A92"/><path d="M0 488 Q290 445 550 470 T800 462 V600 H0 Z" fill="#5B726B"/><g><rect x="310" y="300" width="180" height="220" rx="90" fill="#DCEBE6" opacity=".55"/><rect x="310" y="300" width="180" height="220" rx="90" fill="none" stroke="#4E6B63" stroke-width="8"/><path d="M330 480 q70 30 140 0 l0 40 l-140 0 Z" fill="#8A6B4A"/><g><path d="M400 480 L400 380" stroke="#4E6B2F" stroke-width="8"/><g fill="#6E9A4E"><ellipse cx="372" cy="420" rx="30" ry="14" transform="rotate(-30 372 420)"/><ellipse cx="428" cy="420" rx="30" ry="14" transform="rotate(30 428 420)"/><ellipse cx="400" cy="388" rx="26" ry="13"/></g></g><circle cx="360" cy="500" r="10" fill="#A89880"/><circle cx="440" cy="505" r="12" fill="#98866E"/></g></g>`},
    terra:    {sky:['#D8E4DE','#A8C4BA'], sun:'#F4FBF8', hills:['#86A198','#61766E'],
      draw:`<g><circle cx="620" cy="130" r="52" fill="#F4FBF8" opacity=".9"/><path d="M0 440 Q230 390 450 420 T800 410 V600 H0 Z" fill="#86A198"/><path d="M0 492 Q290 448 550 474 T800 466 V600 H0 Z" fill="#61766E"/><g><ellipse cx="400" cy="470" rx="110" ry="90" fill="#DCEBE6" opacity=".5"/><ellipse cx="400" cy="470" rx="110" ry="90" fill="none" stroke="#4E6B63" stroke-width="8"/><path d="M310 500 q90 34 180 0 l0 42 l-180 0 Z" fill="#8A6B4A"/><path d="M400 500 L400 410" stroke="#4E6B2F" stroke-width="8"/><g fill="#6E9A4E"><ellipse cx="375" cy="445" rx="28" ry="13" transform="rotate(-28 375 445)"/><ellipse cx="425" cy="445" rx="28" ry="13" transform="rotate(28 425 445)"/></g></g></g>`},
    terra2:   {sky:['#C9DCD6','#96B8AC'], sun:'#EFF8F4', hills:['#79948A','#576B62'],
      draw:`<g><circle cx="200" cy="130" r="50" fill="#EFF8F4" opacity=".9"/><path d="M0 445 Q230 395 450 425 T800 415 V600 H0 Z" fill="#79948A"/><path d="M0 495 Q290 452 550 478 T800 470 V600 H0 Z" fill="#576B62"/><g><rect x="330" y="330" width="140" height="180" rx="70" fill="#DCEBE6" opacity=".55"/><rect x="330" y="330" width="140" height="180" rx="70" fill="none" stroke="#4E6B63" stroke-width="8"/><path d="M345 470 q55 24 110 0 l0 40 l-110 0 Z" fill="#8A6B4A"/><circle cx="400" cy="430" r="26" fill="#6E9A4E"/><circle cx="372" cy="455" r="16" fill="#8AB872"/><circle cx="428" cy="455" r="16" fill="#5E8A44"/></g></g>`},
    wreath:   {sky:['#3E5A44','#22331F'], sun:'#F2E4C0', hills:['#2E442F','#1B281B'],
      draw:`<g><circle cx="400" cy="120" r="48" fill="#F2E4C0" opacity=".85"/><path d="M0 450 Q240 405 470 435 T800 425 V600 H0 Z" fill="#2E442F"/><path d="M0 505 Q290 468 560 492 T800 484 V600 H0 Z" fill="#1B281B"/><g><g fill="#3E6B3A"><ellipse cx="400" cy="330" rx="130" ry="34"/><ellipse cx="400" cy="470" rx="130" ry="34"/><ellipse cx="270" cy="400" rx="34" ry="130"/><ellipse cx="530" cy="400" rx="34" ry="130"/></g><g fill="#4E7D46"><ellipse cx="400" cy="348" rx="112" ry="26"/><ellipse cx="400" cy="452" rx="112" ry="26"/><ellipse cx="288" cy="400" rx="26" ry="112"/><ellipse cx="512" cy="400" rx="26" ry="112"/></g><g fill="#C96A3A"><circle cx="330" cy="350" r="11"/><circle cx="470" cy="450" r="11"/><circle cx="470" cy="350" r="11"/><circle cx="330" cy="450" r="11"/></g><g fill="#D9A13B"><circle cx="400" cy="330" r="9"/><circle cx="400" cy="470" r="9"/><circle cx="270" cy="400" r="9"/><circle cx="530" cy="400" r="9"/></g><path d="M400 470 q-4 40 -30 52 M400 470 q4 40 30 52" stroke="#A33E22" stroke-width="12" fill="none"/></g></g>`},
    wreath2:  {sky:['#465E48','#263423'], sun:'#F4E6C2', hills:['#33482F','#1E2B1C'],
      draw:`<g><circle cx="620" cy="120" r="46" fill="#F4E6C2" opacity=".85"/><path d="M0 450 Q240 405 470 435 T800 425 V600 H0 Z" fill="#33482F"/><path d="M0 505 Q290 468 560 492 T800 484 V600 H0 Z" fill="#1E2B1C"/><g><circle cx="400" cy="430" r="110" fill="none" stroke="#3E6B3A" stroke-width="46"/><circle cx="400" cy="430" r="110" fill="none" stroke="#4E7D46" stroke-width="30"/><g fill="#C96A3A"><circle cx="320" cy="380" r="10"/><circle cx="480" cy="480" r="10"/><circle cx="480" cy="380" r="10"/><circle cx="320" cy="480" r="10"/></g><path d="M400 540 q-30 8 -44 34 M400 540 q30 8 44 34" stroke="#A33E22" stroke-width="12" fill="none"/></g></g>`}
  };
  const c = S[key] || S.pottery;
  return `<svg viewBox="0 0 800 600" preserveAspectRatio="xMidYMid slice" role="img" aria-label="${label||'workshop artwork'}">`+
    `<defs><linearGradient id="sk${key}" x1="0" y1="0" x2="0" y2="1">`+
    `<stop offset="0" stop-color="${c.sky[0]}"/><stop offset="1" stop-color="${c.sky[1]}"/></linearGradient>`+
    `<filter id="gr${key}"><feTurbulence type="fractalNoise" baseFrequency=".8" numOctaves="2" result="n"/><feColorMatrix in="n" values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 .06 0"/></filter></defs>`+
    `<rect width="800" height="600" fill="url(#sk${key})"/>${c.draw}`+
    `<rect width="800" height="600" filter="url(#gr${key})"/></svg>`;
}

/* map thumbnail: stylized street map */
function mapSVG(addr){
  return `<svg viewBox="0 0 800 200" preserveAspectRatio="xMidYMid slice" role="img" aria-label="Map">`+
  `<rect width="800" height="200" fill="#EDE4D0"/>`+
  `<g stroke="#D8C9AC" stroke-width="14"><path d="M0 60 H800"/><path d="M0 140 H800"/><path d="M180 0 V200"/><path d="M420 0 V200"/><path d="M640 0 V200"/></g>`+
  `<g stroke="#FFFDF7" stroke-width="8"><path d="M0 60 H800"/><path d="M0 140 H800"/><path d="M180 0 V200"/><path d="M420 0 V200"/><path d="M640 0 V200"/></g>`+
  `<g fill="#B7CBA0"><rect x="200" y="80" width="200" height="40" rx="8"/><rect x="660" y="20" width="120" height="30" rx="8"/></g>`+
  `<g fill="#A9C3D4"><rect x="440" y="20" width="180" height="28" rx="14"/></g>`+
  `<g transform="translate(400,100)"><circle r="34" fill="#BE5A2C" opacity=".18"/><circle r="20" fill="#BE5A2C" opacity=".3"/><path d="M0 -32 C -18 -32 -26 -20 -26 -10 C -26 6 0 22 0 22 C 0 22 26 6 26 -10 C 26 -20 18 -32 0 -32 Z" fill="#BE5A2C"/><circle cy="-11" r="9" fill="#FFF6E8"/></g>`+
  `</svg>`;
}

/* host avatar: initials on warm gradient */
const AVC = [['#BE5A2C','#8A3E1C'],['#6E7D5B','#4A5739'],['#C9962E','#96702A'],['#5E3A52','#3E2536'],['#5F7F92','#3E5A68']];
function avatar(name, sz){
  let h=0; for(const ch of name) h=(h*31+ch.charCodeAt(0))>>>0;
  const c = AVC[h%AVC.length];
  const ini = name.split(/[\s.]+/).filter(Boolean).slice(0,2).map(w=>w[0].toUpperCase()).join('');
  return `<span class="avatar" style="background:linear-gradient(135deg,${c[0]},${c[1]})${sz?`;width:${sz}px;height:${sz}px`:''}">${ini}</span>`;
}
function stars(r){ const f=Math.round(r); return '★'.repeat(f)+'☆'.repeat(5-f); }
function esc(s){ return String(s).replace(/[&<>"]/g, m=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[m])); }
function money(n){ return '$'+n; }

/* ============================== ROUTER ============================== */
const view = document.getElementById('view');
const tabbar = document.getElementById('tabbar');
const toastEl = document.getElementById('toast');
let toastT;
function toast(msg){ toastEl.textContent = msg; toastEl.classList.add('show'); clearTimeout(toastT); toastT = setTimeout(()=>toastEl.classList.remove('show'), 2600); }

const TABS = [
  {id:'discover', label:'Discover', icon:'<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><circle cx="12" cy="12" r="9"/><path d="M15.5 8.5l-2.3 5.2-5.2 2.3 2.3-5.2z" fill="currentColor" stroke="none"/></svg>'},
  {id:'feed', label:'Feed', icon:'<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><rect x="3.5" y="3.5" width="7" height="7" rx="2"/><rect x="13.5" y="3.5" width="7" height="7" rx="2"/><rect x="3.5" y="13.5" width="7" height="7" rx="2"/><rect x="13.5" y="13.5" width="7" height="7" rx="2"/></svg>'},
  {id:'sunny', label:'Sunny', icon:'<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><circle cx="12" cy="12" r="4.2" fill="currentColor" stroke="none"/><path d="M12 2.5v2.6M12 18.9v2.6M2.5 12h2.6M18.9 12h2.6M5.3 5.3l1.8 1.8M16.9 16.9l1.8 1.8M18.7 5.3l-1.8 1.8M7.1 16.9l-1.8 1.8"/></svg>'},
  {id:'calendar', label:'Seasons', icon:'<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><rect x="3.5" y="5" width="17" height="15.5" rx="3"/><path d="M3.5 10h17M8 2.8V6.5M16 2.8V6.5"/></svg>'},
  {id:'host', label:'Host', icon:'<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><circle cx="12" cy="12" r="9"/><path d="M12 8v8M8 12h8"/></svg>'}
];
function renderTabs(active){
  tabbar.innerHTML = TABS.map(t=>`<button class="tab${t.id===active?' on':''}" data-go="#/${t.id}" aria-label="${t.label}">${t.icon}<span>${t.label}</span></button>`).join('');
}
document.addEventListener('click', e=>{
  const g = e.target.closest('[data-go]');
  if(g){ location.hash = g.dataset.go; }
});

const F = { q:'', cat:'All', maxPrice:200, maxDist:50, when:'any' };
function filtered(){
  return WORKSHOPS.filter(w=>{
    if(F.cat!=='All' && w.cat!==F.cat) return false;
    if(w.price>F.maxPrice) return false;
    if(w.dist>F.maxDist) return false;
    if(F.q){ const q=F.q.toLowerCase(); if(!(w.title+' '+w.host+' '+w.cat+' '+w.area+' '+w.tags.join(' ')).toLowerCase().includes(q)) return false; }
    if(F.when==='weekend' && !w.tags.includes('weekend')) return false;
    return true;
  }).sort((a,b)=> b.rating-a.rating);
}
function wcard(w,i){
  const s = w.slots[0];
  return `<a class="wcard grain ${tilt(i)}" data-go="#/w/${w.id}" href="#/w/${w.id}">
    <div class="wcard-art">${scene(w.motif, w.title)}
      <span class="price-badge">${money(w.price)}</span>
      <span class="dist-badge">◈ ${w.dist} mi</span>
      ${stickers(w)}
    </div>
    <div class="wcard-body">
      <div class="wcard-cat">${w.cat} · ${w.dur}</div>
      <div class="wcard-title serif">${esc(w.title)}</div>
      <div class="wcard-meta"><span class="stars">${stars(w.rating)}</span><b>${w.rating}</b><span>(${w.rc})</span><span>·</span><span>${esc(w.area)}</span></div>
      <div class="wcard-next"><span class="dot"></span><span>Next: <b>${s.d} · ${s.t}</b> — ${s.left} spots left</span></div>
    </div></a>`;
}

/* ---------- playful bits: stickers, tilt, rails, confetti ---------- */
function tilt(i){ return ((i||0)%2) ? 'tilt-r' : 'tilt-l'; }
function stickers(w){
  const s=w.slots[0]; const out=[];
  if(s.left<=3) out.push('<span class="sticker hot">🔥 Selling fast</span>');
  else if(w.isNew) out.push('<span class="sticker new">✨ New</span>');
  if(w.tags.includes('date idea')) out.push('<span class="sticker date">💛 Date-night fave</span>');
  else if(w.tags.includes('weekend')) out.push('<span class="sticker wknd">🎉 Weekend pick</span>');
  const two=out.slice(0,2);
  return two.length?`<div class="stickers">${two.join('')}</div>`:'';
}
function railCard(w,i){
  const s=w.slots[0];
  return `<a class="rcard ${tilt(i)}" data-go="#/w/${w.id}" href="#/w/${w.id}">
    <span class="rcard-art">${scene(w.motif,w.title)}${stickers(w)}</span>
    <span class="rcard-body"><span class="rcard-t">${esc(w.title)}</span>
    <span class="rcard-m">${money(w.price)} · ${w.dur} · <b style="color:var(--clay-deep)">${s.left} left</b></span></span>
  </a>`;
}
function hotWeekend(list){
  return list.filter(w=>w.tags.includes('weekend'))
    .sort((a,b)=>a.slots[0].left-b.slots[0].left).slice(0,6);
}
function confettiBurst(){
  const c=document.getElementById('confetti'); if(!c) return;
  const colors=['#D14D1F','#F2A413','#B23A5E','#2F7D6B','#6B3A5E','#FFFDF6'];
  let html='';
  for(let i=0;i<56;i++){
    const x=(Math.random()*100).toFixed(1), d=(1.1+Math.random()*1.1).toFixed(2), dl=(Math.random()*0.6).toFixed(2);
    const col=colors[i%colors.length], rr=((Math.random()*360)|0)+'deg', sz=6+((Math.random()*7)|0);
    const round=Math.random()>0.55?'50%':'2px';
    html+='<i style="left:'+x+'%;width:'+sz+'px;height:'+(round==='50%'?sz:Math.round(sz*0.55))+'px;background:'+col+';border-radius:'+round+';--rr:'+rr+';animation-duration:'+d+'s;animation-delay:'+dl+'s"></i>';
  }
  c.innerHTML=html;
  setTimeout(()=>{ const cc=document.getElementById('confetti'); if(cc) cc.innerHTML=''; }, 3400);
}
function bindDone(){ confettiBurst(); }

/* ---------- DISCOVER ---------- */
function vDiscover(){
  const list = filtered();
  const hour = new Date().getHours();
  const greet = hour<12?'Good morning':hour<17?'Good afternoon':'Good evening';
  return `
  <div class="topbar"><div class="topbar-in">
    <div class="brandmark"><svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#FFF6E8" stroke-width="2"><path d="M12 21c-5 0-8-3-8-8 0-4 3-6 5-8-1 3 0 5 3 5s4-2 3-5c2 2 5 4 5 8 0 5-3 8-8 8z"/><path d="M12 21v-6"/></svg></div>
    <div><div class="brandname">Hearth <em>&</em> Hand</div><div class="tagline">Workshops near you</div></div>
  </div></div>
  <div class="hero">
    <div class="doodles" aria-hidden="true">
      <svg class="doodle d1" width="30" height="30" viewBox="0 0 30 30"><path d="M15 2l2.6 8.4L26 13l-8.4 2.6L15 24l-2.6-8.4L4 13l8.4-2.6z" fill="#F2A413"/></svg>
      <svg class="doodle d2" width="22" height="22" viewBox="0 0 22 22"><circle cx="11" cy="11" r="6" fill="none" stroke="#B23A5E" stroke-width="3"/></svg>
      <svg class="doodle d3" width="36" height="14" viewBox="0 0 36 14"><path d="M2 10 Q9 2 17 8 T34 6" fill="none" stroke="#2F7D6B" stroke-width="3" stroke-linecap="round"/></svg>
    </div>
    <span class="hand-eyebrow">✳ fresh picks for your weekend</span>
    <h1>${greet}.<br>Go <span class="marker">make something</span><br><span class="accent">with your hands.</span></h1>
    <p class="sub">Every listing shows price, timings &amp; what&rsquo;s included upfront. No &ldquo;DM for price&rdquo; — ever.</p>
    <div class="shuffle-row"><button class="shuffle" id="surpriseBtn"><span class="dice">🎲</span> Surprise me</button>
      <span class="hand" style="font-size:14px;color:var(--muted)">feeling lucky?</span></div>
  </div>
  <div class="torn" aria-hidden="true"><svg viewBox="0 0 432 26" preserveAspectRatio="none"><path d="M0 26 L0 14 L18 20 L36 10 L54 18 L72 8 L90 18 L108 10 L126 19 L144 9 L162 18 L180 10 L198 19 L216 9 L234 18 L252 10 L270 19 L288 9 L306 18 L324 10 L342 19 L360 9 L378 18 L396 10 L414 19 L432 12 L432 26 Z" fill="#F2A413"/></svg></div>
  <div class="pad" style="padding-top:14px">
    <div class="searchbar"><span style="color:var(--faint);font-size:17px">⌕</span>
      <input id="q" type="search" placeholder="Try &quot;pottery&quot;, &quot;pasta&quot;, &quot;date night&quot;&hellip;" value="${esc(F.q)}" aria-label="Search workshops"></div>
    <div class="filters">
      <select class="fselect" id="fWhen" aria-label="When"><option value="any">Any date</option><option value="weekend"${F.when==='weekend'?' selected':''}>This weekend</option></select>
      <select class="fselect" id="fPrice" aria-label="Max price"><option value="200">Any price</option><option value="40"${F.maxPrice==40?' selected':''}>Under $40</option><option value="70"${F.maxPrice==70?' selected':''}>Under $70</option><option value="100"${F.maxPrice==100?' selected':''}>Under $100</option></select>
      <select class="fselect" id="fDist" aria-label="Distance"><option value="50">Any distance</option><option value="5"${F.maxDist==5?' selected':''}>Under 5 mi</option><option value="10"${F.maxDist==10?' selected':''}>Under 10 mi</option></select>
    </div>
  </div>
  <div class="chips" id="cats">${CATS.map(c=>`<button class="chip${F.cat===c?' on':''}" data-cat="${c}">${c}</button>`).join('')}</div>
  ${hotWeekend(list).length>1?`<div class="section" style="margin-top:20px">
    <div class="sec-head" style="padding:0 18px"><h2>🔥 Hot this weekend</h2><span class="hand">swipe →</span></div>
    <div class="rail">${hotWeekend(list).map((w,i)=>railCard(w,i)).join('')}</div>
  </div>`:''}
  <a class="season-strip grain" data-go="#/calendar" href="#/calendar" style="text-decoration:none">
    ${scene('pumpkin','Harvest season')}
    <span class="washi" aria-hidden="true"></span>
    <div class="txt"><h3 class="serif">🍂 Harvest Season is here</h3><p>Pumpkin picking, cider days &amp; autumn crafts — only in October.</p></div>
  </a>
  <div class="section pad" style="padding-top:18px">
    <div class="sec-head"><h2>${F.cat==='All'?'Near you':F.cat}</h2><span class="hand">tap one, go make →</span></div>
    <div class="cards">${list.length?list.map(wcard).join(''):`<div class="empty"><div class="big">🏺</div><div class="hand">Hmm, the basket&rsquo;s empty…</div><p>No workshops match those filters.<br>Try loosening one or two.</p></div>`}</div>
  </div>
  <div class="foot-note">Prototype — sample listings for demonstration.<br>Hearth &amp; Hand · Made with clay, flour &amp; yarn</div>`;
}
function bindDiscover(){
  const q=document.getElementById('q'); if(!q) return;
  q.addEventListener('input', ()=>{ F.q=q.value; rerender(false); const nq=document.getElementById('q'); nq.focus(); nq.setSelectionRange(nq.value.length,nq.value.length); });
  document.getElementById('fWhen').addEventListener('change', e=>{F.when=e.target.value; rerender();});
  document.getElementById('fPrice').addEventListener('change', e=>{F.maxPrice=+e.target.value; rerender();});
  document.getElementById('fDist').addEventListener('change', e=>{F.maxDist=+e.target.value; rerender();});
  document.getElementById('cats').addEventListener('click', e=>{ const c=e.target.closest('[data-cat]'); if(c){F.cat=c.dataset.cat; rerender();} });
  const sb=document.getElementById('surpriseBtn');
  if(sb) sb.onclick=()=>{ const w=WORKSHOPS[(Math.random()*WORKSHOPS.length)|0];
    toast('🎲 The universe chose…'); setTimeout(()=>{ location.hash='#/w/'+w.id; }, 420); };
}

/* ---------- DETAIL ---------- */
function vDetail(id){
  const w = WORKSHOPS.find(x=>x.id===id); if(!w) return vDiscover();
  if(BK.wid!==id){ BK.wid=id; BK.slot=0; }
  const s0 = w.slots[0];
  return `
  <div class="topbar"><div class="topbar-in">
    <button class="backbtn" data-go="#/discover" aria-label="Back">←</button>
    <div class="topbar-title">${w.cat}</div>
  </div></div>
  <div class="detail-hero grain">${scene(w.motif, w.title)}<span class="price-badge">${money(w.price)} <span style="font-weight:400;font-size:12px">/ person</span></span>${stickers(w)}</div>
  <div class="detail-body">
    <div class="detail-cat">${w.cat} · ${esc(w.area)} · ${w.dist} mi away</div>
    <h1>${esc(w.title)}</h1>
    <div class="wcard-meta" style="font-size:14px"><span class="stars">${stars(w.rating)}</span><b>${w.rating}</b><span>(${w.rc} reviews)</span></div>
    <div class="hostrow">${avatar(w.host)}<div><div class="nm">${esc(w.host)}</div><div class="sub">Hosting since ${w.hostSince}</div></div></div>
    <p class="lede">${esc(w.desc)}</p>
    <div class="section"><div class="sec-head"><h2>The essentials</h2></div>
      <div class="info-grid">
        <div class="info-cell"><div class="k">Price</div><div class="v">${money(w.price)} / person</div></div>
        <div class="info-cell"><div class="k">Duration</div><div class="v">${w.dur}</div></div>
        <div class="info-cell"><div class="k">Location</div><div class="v">${esc(w.area)}</div></div>
        <div class="info-cell"><div class="k">Rating</div><div class="v">${w.rating} ★ (${w.rc})</div></div>
      </div>
      <div class="mapcard grain">${mapSVG(w.address)}<div class="addr">📍 ${esc(w.address)}</div></div>
    </div>
    <div class="section"><div class="sec-head"><h2>What&rsquo;s included</h2></div>
      <ul class="included">${w.included.map(i=>`<li><span class="tick">✓</span><span>${esc(i)}</span></li>`).join('')}</ul>
    </div>
    <div class="section"><div class="sec-head"><h2>Pick a slot</h2></div>
      <div class="slot-pick" id="slots">${w.slots.map((s,i)=>`<button class="slot${i===BK.slot?' on':''}${s.left===0?' soldout':''}" data-slot="${i}"${s.left===0?' disabled':''}><div class="d">${s.d}</div><div class="t">${s.t}</div><div class="left">${s.left>0?s.left+' spots left':'Sold out'}</div></button>`).join('')}</div>
    </div>
    <div class="section"><div class="sec-head"><h2>Reviews</h2><span style="font-size:13px;color:var(--muted)">${w.rc} total</span></div>
      ${w.reviews.map(r=>`<div class="review">${avatar(r.n)}<div style="flex:1"><div class="rnm">${esc(r.n)}</div><div class="rmeta"><span class="stars">${stars(r.s)}</span> · ${r.w}</div><p class="txt">${esc(r.t)}</p><div class="rphoto grain">${scene(r.m,r.n+'’s photo')}</div></div></div>`).join('')}
    </div>
    <div class="section"><div class="sec-head"><h2>About the host</h2></div><p class="lede">${esc(w.hostBlurb)}</p></div>
    <div style="height:110px"></div>
  </div>
  <div class="bookbar"><div class="pr"><div class="a">${money(w.price)}</div><div class="b">per person · ${w.dur}</div></div>
    <button class="btn" data-go="#/book/${w.id}">Book ${s0.left} spots left</button></div>`;
}
function bindDetail(id){
  const sp=document.getElementById('slots'); if(!sp) return;
  sp.addEventListener('click', e=>{ const b=e.target.closest('[data-slot]'); if(!b||b.disabled) return; BK.slot=+b.dataset.slot; BK.wid=id; rerender(false); });
}

/* ---------- BOOKING ---------- */
const BK = { wid:null, slot:0, seats:2, name:'', email:'' };
function vBook(id){
  const w = WORKSHOPS.find(x=>x.id===id); if(!w) return vDiscover();
  if(BK.wid!==id){ BK.wid=id; BK.slot=0; BK.seats=2; }
  const s = w.slots[BK.slot], total = w.price*BK.seats;
  return `
  <div class="topbar"><div class="topbar-in">
    <button class="backbtn" data-go="#/w/${id}" aria-label="Back">←</button>
    <div class="topbar-title">Book your spot</div>
  </div></div>
  <div class="pad">
    <div class="steps"><div class="stepdot on"></div><div class="stepdot on"></div><div class="stepdot"></div></div>
    <div class="summary">
      <div class="row"><span>Workshop</span><b style="text-align:right;max-width:60%">${esc(w.title)}</b></div>
      <div class="row"><span>Slot</span><b>${s.d} · ${s.t}</b></div>
      <div class="row"><span>Duration</span><b>${w.dur}</b></div>
      <div class="row"><span>Location</span><b>${esc(w.area)}</b></div>
    </div>
    <div class="field"><label>How many seats?</label>
      <div class="stepper"><button id="minus" aria-label="Fewer seats">−</button><span class="n" id="seatN">${BK.seats}</span><button id="plus" aria-label="More seats">+</button>
      <span class="hint" style="margin:0">${s.left} spots left in this slot</span></div></div>
    <div class="field"><label>Your name</label><input class="input" id="bkName" placeholder="Jordan Ellis" value="${esc(BK.name)}"></div>
    <div class="field"><label>Email for confirmation</label><input class="input" id="bkEmail" type="email" placeholder="you@example.com" value="${esc(BK.email)}"></div>
    <div class="summary"><div class="row"><span>${money(w.price)} × ${BK.seats} seats</span><span>${money(total)}</span></div>
      <div class="row"><span>Booking fee</span><span>Free</span></div>
      <div class="row total"><span>Total</span><span>${money(total)}</span></div></div>
    <button class="btn" id="toPay">Continue to payment →</button>
    <p class="hint" style="text-align:center;margin-top:10px">Free cancellation up to 48h before · Instant confirmation</p>
  </div>`;
}
function bindBook(id){
  const w = WORKSHOPS.find(x=>x.id===id);
  const upd=()=>rerender(false);
  document.getElementById('minus').onclick=()=>{ if(BK.seats>1){BK.seats--; upd();} };
  document.getElementById('plus').onclick=()=>{ const s=w.slots[BK.slot]; if(BK.seats<Math.min(s.left,8)){BK.seats++; upd();} else toast('Only '+s.left+' spots left in this slot'); };
  document.getElementById('toPay').onclick=()=>{
    BK.name=document.getElementById('bkName').value.trim(); BK.email=document.getElementById('bkEmail').value.trim();
    if(!BK.name){ toast('Please add your name'); return; }
    if(!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(BK.email)){ toast('Please add a valid email'); return; }
    location.hash='#/pay/'+id;
  };
}
function vPay(id){
  const w = WORKSHOPS.find(x=>x.id===id); if(!w) return vDiscover();
  const s=w.slots[BK.slot], total=w.price*BK.seats;
  return `
  <div class="topbar"><div class="topbar-in">
    <button class="backbtn" data-go="#/book/${id}" aria-label="Back">←</button>
    <div class="topbar-title">Payment</div>
  </div></div>
  <div class="pad">
    <div class="steps"><div class="stepdot on"></div><div class="stepdot on"></div><div class="stepdot on"></div></div>
    <div class="testmode"><span style="font-size:20px">🧪</span><span><b>Test mode</b> — no real charge. Use card 4242 4242 4242 4242, any future date, any CVC.</span></div>
    <div class="field"><label>Card number</label><input class="input" id="cc" inputmode="numeric" placeholder="4242 4242 4242 4242" maxlength="19"></div>
    <div style="display:flex;gap:10px"><div class="field" style="flex:1"><label>Expiry</label><input class="input" placeholder="12 / 28" maxlength="7"></div>
    <div class="field" style="flex:1"><label>CVC</label><input class="input" placeholder="123" maxlength="4" inputmode="numeric"></div></div>
    <div class="summary"><div class="row"><span>${esc(w.title)}</span></div>
      <div class="row"><span>${s.d} · ${s.t} · ${BK.seats} seat${BK.seats>1?'s':''}</span></div>
      <div class="row total"><span>Total due</span><span>${money(total)}</span></div></div>
    <button class="btn" id="payBtn">Pay ${money(total)}</button>
    <p class="hint" style="text-align:center;margin-top:10px">🔒 Encrypted checkout · Powered by Stripe (test)</p>
  </div>`;
}
function bindPay(id){
  const cc=document.getElementById('cc');
  cc.addEventListener('input', ()=>{ cc.value=cc.value.replace(/\D/g,'').slice(0,16).replace(/(.{4})/g,'$1 ').trim(); });
  document.getElementById('payBtn').onclick=()=>{
    if(cc.value.replace(/\s/g,'').length<16){ toast('Please enter the test card number'); return; }
    const btn=document.getElementById('payBtn'); btn.textContent='Processing…'; btn.disabled=true;
    setTimeout(()=>{ location.hash='#/done/'+id; }, 1400);
  };
}
function vDone(id){
  const w = WORKSHOPS.find(x=>x.id===id); if(!w) return vDiscover();
  const s=w.slots[BK.slot], total=w.price*BK.seats;
  const ref='HH-'+Math.random().toString(36).slice(2,7).toUpperCase();
  return `
  <div class="confetti" id="confetti" aria-hidden="true"></div>
  <div class="topbar"><div class="topbar-in"><div class="topbar-title">You&rsquo;re in!</div></div></div>
  <div class="confirm-hero"><div class="checkring">✓</div>
    <h1 class="serif" style="font-size:29px">Booking confirmed 🎉</h1>
    <p class="lede" style="margin-top:8px">See you at the ${w.cat.toLowerCase()} table, ${esc(BK.name.split(' ')[0]||'maker')}.</p>
    <div class="refcode">${ref}</div>
  </div>
  <div class="ticket"><div class="t-art grain">${scene(w.motif,w.title)}</div>
    <div class="t-body">
      <h3 class="serif" style="font-size:18px;margin-bottom:8px">${esc(w.title)}</h3>
      <div class="t-row"><span>When</span><b>${s.d} · ${s.t}</b></div>
      <div class="t-row"><span>Where</span><b>${esc(w.area)}</b></div>
      <div class="t-row"><span>Seats</span><b>${BK.seats}</b></div>
      <div class="t-row"><span>Paid (test)</span><b>${money(total)}</b></div>
      <div class="perf"></div>
      <div class="t-row"><span>Receipt sent to</span><b>${esc(BK.email)}</b></div>
    </div></div>
  <div class="pad">
    <button class="btn sage" data-go="#/feed">See what others made →</button>
    <div style="height:10px"></div>
    <button class="btn ghost" data-go="#/discover">Back to Discover</button>
  </div>`;
}

/* ---------- HOST FLOW ---------- */
const HOST = { step:0, idea:'', cat:'Pottery', dur:'2 hours', price:'', loc:'', seats:'6', inc:'', gen:null };
const HOST_CATS = ['Pottery','Food','Fiber','Art','Garden','Dance','Seasonal'];
function vHost(){
  if(HOST.step===0) return `
  <div class="topbar"><div class="topbar-in"><div class="topbar-title">Become a host</div></div></div>
  <div class="host-hero">
    <div class="badge-ic"><svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="#FFF6E8" stroke-width="1.8"><path d="M12 21c-5 0-8-3-8-8 0-4 3-6 5-8-1 3 0 5 3 5s4-2 3-5c2 2 5 4 5 8 0 5-3 8-8 8z"/></svg></div>
    <h1 class="serif">Turn your craft into a workshop</h1>
    <p class="lede">Describe your idea in plain words — our Listing Studio writes the copy, designs your poster and sets up booking. Takes about 2 minutes.</p>
  </div>
  <div class="rule-card"><span style="font-size:20px">📜</span><span><b>Our founding rule:</b> every listing must show price, timings, location, duration &amp; what&rsquo;s included upfront. If anything&rsquo;s missing, it doesn&rsquo;t go live.</span></div>
  <div class="pad"><button class="btn" id="hostStart">Describe my workshop idea →</button>
  <div style="height:10px"></div><button class="btn ghost" id="hostHow">How it works</button>
  <div id="howBox" style="display:none;margin-top:14px" class="preview-card"><p class="lede" style="font-size:14px">1. You describe the idea (2 min)<br>2. Studio drafts title, description, slots &amp; poster<br>3. You review the completeness check<br>4. Publish — bookings open instantly</p></div></div>`;
  if(HOST.step===1) return `
  <div class="topbar"><div class="topbar-in"><button class="backbtn" id="hBack" aria-label="Back">←</button><div class="topbar-title">Your idea · step 1 of 2</div></div></div>
  <div class="pad">
    <div class="field"><label>What do you want to teach?</label>
      <textarea class="input" id="hIdea" placeholder="e.g. I make soy candles in my kitchen and want to teach a cozy autumn evening class where everyone pours two candles…">${esc(HOST.idea)}</textarea>
      <p class="hint">Just ramble — the Studio tidies it up.</p></div>
    <div class="field"><label>Category</label><select class="input" id="hCat">${HOST_CATS.map(c=>`<option${c===HOST.cat?' selected':''}>${c}</option>`).join('')}</select></div>
    <button class="btn" id="hNext1">Continue →</button></div>`;
  if(HOST.step===2) return `
  <div class="topbar"><div class="topbar-in"><button class="backbtn" id="hBack" aria-label="Back">←</button><div class="topbar-title">Logistics · step 2 of 2</div></div></div>
  <div class="pad">
    <div class="field"><label>Duration</label><select class="input" id="hDur">${['1 hour','1.5 hours','2 hours','2.5 hours','3 hours','Full day','Multi-day'].map(d=>`<option${d===HOST.dur?' selected':''}>${d}</option>`).join('')}</select></div>
    <div style="display:flex;gap:10px"><div class="field" style="flex:1"><label>Price per person ($)</label><input class="input" id="hPrice" inputmode="numeric" placeholder="55" value="${esc(HOST.price)}"></div>
    <div class="field" style="flex:1"><label>Max seats</label><input class="input" id="hSeats" inputmode="numeric" placeholder="8" value="${esc(HOST.seats)}"></div></div>
    <div class="field"><label>Where will it happen?</label><input class="input" id="hLoc" placeholder="e.g. 12 Maple Ave, Cambridge MA" value="${esc(HOST.loc)}"></div>
    <div class="field"><label>What&rsquo;s included? <span style="text-transform:none;letter-spacing:0">(one per line)</span></label>
      <textarea class="input" id="hInc" placeholder="Soy wax + two vessels&#10;All wicks, scents & tools&#10;Mulled cider">${esc(HOST.inc)}</textarea></div>
    <button class="btn" id="hGenerate">✨ Generate my listing</button>
    <p class="hint" style="text-align:center;margin-top:10px">The Studio drafts everything — you approve before anything goes live.</p></div>`;
  if(HOST.step===3) return `
  <div class="topbar"><div class="topbar-in"><div class="topbar-title">Listing Studio</div></div></div>
  <div class="gen-wrap"><div class="gen-poster grain">${scene('pottery','generating')}</div>
    <div class="typeline" id="typeLine"></div><div class="genbar"><i id="genFill"></i></div></div>`;
  if(HOST.step===4 && HOST.gen){
    const g=HOST.gen;
    const checks=[['Price',!!g.price],['Timings',!!g.slot],['Location',!!g.loc],['Duration',!!g.dur],['What\u2019s included',g.inc.length>0]];
    const allOk=checks.every(c=>c[1]);
    return `
  <div class="topbar"><div class="topbar-in"><button class="backbtn" id="hBack" aria-label="Back">←</button><div class="topbar-title">Review your listing</div></div></div>
  <div class="pad">
    <div class="poster grain" style="margin-bottom:16px">${posterSVG(g)}</div>
    <div class="preview-card"><div class="eyebrow">Generated title</div><h3 class="serif">${esc(g.title)}</h3>
      <p class="lede" style="font-size:14px">${esc(g.desc)}</p></div>
    <div class="info-grid">
      <div class="info-cell"><div class="k">Price</div><div class="v">${g.price?money(g.price)+' / person':'—'}</div></div>
      <div class="info-cell"><div class="k">Duration</div><div class="v">${g.dur||'—'}</div></div>
      <div class="info-cell"><div class="k">Location</div><div class="v">${esc(g.loc||'—')}</div></div>
      <div class="info-cell"><div class="k">First slot</div><div class="v">${esc(g.slot||'—')}</div></div>
    </div>
    <div class="section"><div class="sec-head"><h2>Completeness check</h2></div>
      <ul class="checklist">${checks.map(c=>`<li class="${c[1]?'':'missing'}"><span class="st ${c[1]?'y':'n'}">${c[1]?'✓':'!'}</span><span>${c[0]} ${c[1]?'— looks good':'— <b>missing</b>'}</span></li>`).join('')}</ul>
      ${allOk?`<p class="hint">✅ All five essentials present. Ready to publish.</p>`:`<div class="rule-card" style="margin:0"><span>⛔</span><span><b>Can&rsquo;t publish yet.</b> Our rule: price, timings, location, duration &amp; inclusions must all be shown upfront. Add what&rsquo;s missing to go live.</span></div>`}
      <p class="hint" style="margin-top:10px"><a href="#" id="demoMissing">See what happens when something&rsquo;s missing →</a></p>
    </div>
    <div class="section"><div class="sec-head"><h2>What&rsquo;s included</h2></div>
      <ul class="included">${g.inc.map(i=>`<li><span class="tick">✓</span><span>${esc(i)}</span></li>`).join('')||'<li><span class="tick">!</span><span>Nothing listed yet</span></li>'}</ul></div>
    <button class="btn" id="hPublish" ${allOk?'':'disabled'}>${allOk?'Publish my workshop 🚀':'Complete the missing fields to publish'}</button>
    <div style="height:8px"></div><button class="btn ghost" id="hEdit">Edit details</button>
  </div>`;
  }
  if(HOST.step===5) return `
  <div class="topbar"><div class="topbar-in"><div class="topbar-title">Published</div></div></div>
  <div class="confirm-hero"><div class="checkring">✓</div>
    <h1 class="serif" style="font-size:28px">You&rsquo;re live!</h1>
    <p class="lede" style="margin-top:8px">Your workshop passed the completeness check and is now bookable.</p></div>
  <div class="pad"><button class="btn sage" data-go="#/discover">View it in Discover →</button>
  <div style="height:10px"></div><button class="btn ghost" id="hAgain">Create another</button></div>`;
  return '';
}
/* AI-ish generation from host inputs (template composition = the "Studio") */
function generateListing(){
  const idea = HOST.idea.trim();
  const first = idea.split('.')[0] || 'a hands-on making session';
  const key = idea.match(/candle/i)?'candle':idea.match(/bread|bake|sourdough/i)?'bread':idea.match(/pot|clay|ceramic/i)?'pottery':idea.match(/knit|yarn|crochet/i)?'knit':idea.match(/paint|watercolor|draw/i)?'paint':idea.match(/garden|plant|terrarium/i)?'garden':'pottery';
  const titleBits = {candle:['Hand-Poured Soy Candles','An Autumn Evening'],bread:['Rustic Bread at Home','From Mix to Crust'],pottery:['Clay & Calm','A Beginner Throwing Session'],knit:['Knit Night','Cozy Stitches for Beginners'],paint:['Paint & Sip (Minus the Pretense)','Autumn in Watercolor'],garden:['Tiny Green Worlds','A Planting Workshop']};
  const tb = titleBits[key];
  const inc = HOST.inc.split('\n').map(s=>s.trim()).filter(Boolean);
  return {
    title: tb[0]+': '+tb[1], motif:key==='candle'?'wreath':key,
    desc:'A welcoming, beginner-friendly session built around your idea — "'+first.trim().slice(0,110)+'". Small group, all materials provided, and you leave with something you made yourself.',
    price: HOST.price? +HOST.price : 0, dur: HOST.dur, loc: HOST.loc.trim(),
    slot:'Sat, Oct 18 · 1:00 PM', seats: HOST.seats||'8', inc
  };
}
function posterSVG(g){
  const cap = g.title.length>44? g.title.slice(0,44)+'…' : g.title;
  return `<svg viewBox="0 0 600 760" role="img" aria-label="Workshop poster">
  <defs><linearGradient id="pp" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="#3E2A1C"/><stop offset="1" stop-color="#1E130C"/></linearGradient></defs>
  <rect width="600" height="760" fill="url(#pp)"/>
  <circle cx="300" cy="250" r="120" fill="#E8B64C" opacity=".9"/><circle cx="300" cy="250" r="150" fill="#E8B64C" opacity=".15"/>
  <g fill="#F6E8C8" opacity=".85"><ellipse cx="300" cy="470" rx="180" ry="60"/><ellipse cx="300" cy="470" rx="120" ry="40" fill="#E4D2A8"/></g>
  <text x="300" y="120" text-anchor="middle" fill="#F6E8C8" font-family="Georgia,serif" font-size="21" letter-spacing="6">HEARTH &amp; HAND</text>
  <text x="300" y="600" text-anchor="middle" fill="#FFF6E8" font-family="Georgia,serif" font-size="34" font-weight="bold"><tspan x="300" dy="0">${esc(cap.split(':')[0])}</tspan>${cap.includes(':')?`<tspan x="300" dy="42" font-size="26" font-style="italic" fill="#E8B64C">${esc(cap.split(':')[1].trim())}</tspan>`:''}</text>
  <text x="300" y="700" text-anchor="middle" fill="#C9B48C" font-family="Georgia,serif" font-size="19">${esc(g.slot)} · ${g.price?'$'+g.price:''} · ${esc((g.loc||'').split(',')[0])}</text>
  </svg>`;
}
function bindHost(){
  const on=(id,fn)=>{const el=document.getElementById(id); if(el) el.onclick=fn;};
  on('hostStart',()=>{HOST.step=1; rerender();});
  on('hostHow',()=>{const b=document.getElementById('howBox'); b.style.display=b.style.display==='none'?'block':'none';});
  on('hBack',()=>{HOST.step=Math.max(0,HOST.step-1); if(HOST.step===3)HOST.step=2; rerender();});
  on('hNext1',()=>{HOST.idea=document.getElementById('hIdea').value; HOST.cat=document.getElementById('hCat').value;
    if(HOST.idea.trim().length<12){toast('Tell us a little more about your idea');return;} HOST.step=2; rerender();});
  on('hGenerate',()=>{HOST.dur=document.getElementById('hDur').value; HOST.price=document.getElementById('hPrice').value.trim();
    HOST.loc=document.getElementById('hLoc').value; HOST.seats=document.getElementById('hSeats').value; HOST.inc=document.getElementById('hInc').value;
    HOST.step=3; rerender(); runGen();});
  on('hEdit',()=>{HOST.step=2; rerender();});
  on('hPublish',()=>{ if(!HOST.gen) return;
    const g=HOST.gen;
    WORKSHOPS.unshift({id:'host-'+Date.now(), cat:HOST.cat, motif:g.motif, title:g.title, host:'You', hostSince:'2026',
      hostBlurb:'Your brand-new studio — welcome aboard!', area:(g.loc.split(',')[0]||'Your neighborhood'), address:g.loc||'TBD',
      dist:0.4, price:g.price, dur:g.dur, rating:5.0, rc:1, tags:['new'], months:['oct','nov'], isNew:true,
      desc:g.desc, included:g.inc, slots:[{d:'Sat, Oct 18',t:'1:00 PM',left:+g.seats||8}],
      reviews:[{n:'Studio note',w:'just now',s:5,t:'This listing passed the completeness check — price, timings, location, duration and inclusions all shown upfront.',m:g.motif}]});
    HOST.step=5; rerender(); toast('🎉 Your workshop is live!'); });
  on('hAgain',()=>{Object.assign(HOST,{step:0,idea:'',price:'',loc:'',inc:'',gen:null}); rerender();});
  const dm=document.getElementById('demoMissing');
  if(dm) dm.onclick=(e)=>{e.preventDefault(); HOST.price=''; HOST.gen=generateListing(); rerender(); bindHost(); toast('Price removed — watch the checklist');};
}
function runGen(){
  const lines=['Reading your idea…','Drafting a title that sings…','Writing the description…','Designing your poster…','Running the completeness check…'];
  const tl=document.getElementById('typeLine'), fill=document.getElementById('genFill');
  let i=0;
  const tick=()=>{
    if(i<lines.length){ tl.textContent=lines[i]; fill.style.width=((i+1)/lines.length*100)+'%'; i++; setTimeout(tick, 750); }
    else { HOST.gen=generateListing(); HOST.step=4; rerender(); }
  }; tick();
}

/* ---------- FEED ---------- */
function vFeed(){
  return `
  <div class="topbar"><div class="topbar-in"><div class="topbar-title">What people made</div></div></div>
  <div class="pad" style="padding-bottom:0"><p class="lede">Real photos from last weekend&rsquo;s workshops — the <b>social</b> side of learning. <span class="tagpill">📸 ${FEED.length} new</span></p></div>
  <div class="pad"><div class="cards">
    ${FEED.map((p,i)=>{ const w=WORKSHOPS.find(x=>x.id===p.w); return `
    <article class="post ${tilt(i)}">
      <div class="post-head">${avatar(p.n)}<div><div class="nm">${esc(p.n)}</div><div class="sub">did <b>${esc(w?w.title.split(':')[0]:p.tag)}</b> · ${p.when}</div></div></div>
      <div class="post-art grain">${scene(p.m, p.n+'’s photo')}</div>
      <div class="post-body"><div class="stars">${stars(p.s)}</div><p>${esc(p.t)}</p></div>
      <div class="post-foot"><button class="likebtn" data-like="${i}"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 20.5C7 16.5 3 13.2 3 9.3 3 6.4 5.2 4.5 7.7 4.5c1.7 0 3.3.9 4.3 2.4 1-1.5 2.6-2.4 4.3-2.4 2.5 0 4.7 1.9 4.7 4.8 0 3.9-4 7.2-9 11.2z"/></svg><span>${p.likes}</span></button>
      ${w?`<a href="#/w/${w.id}" data-go="#/w/${w.id}" style="font-size:13px;font-weight:600">Book this workshop →</a>`:''}</div>
    </article>`;}).join('')}
  </div></div>
  <div class="foot-note">Made something? Your photos could inspire someone&rsquo;s weekend.<br>Post after any workshop — free.</div>`;
}
function bindFeed(){
  document.querySelectorAll('[data-like]').forEach(b=>b.onclick=()=>{
    const i=+b.dataset.like, p=FEED[i];
    if(b.classList.contains('liked')){ b.classList.remove('liked'); p.likes--; b.querySelector('span').textContent=p.likes; }
    else { b.classList.add('liked'); p.likes++; b.querySelector('span').textContent=p.likes; }
  });
}

/* ---------- SUNNY ---------- */
const CHAT=[{me:false,t:`Hi, I&rsquo;m Sunny ☀️ — tell me what you&rsquo;re in the mood for, vaguely is fine. Like <i>&ldquo;plan a pottery date this weekend&rdquo;</i> or <i>&ldquo;something cozy under $50&rdquo;</i>.`}];
function sunnyReply(q){
  const s=q.toLowerCase();
  const pick=(ids)=>ids.map(id=>WORKSHOPS.find(w=>w.id===id)).filter(Boolean).slice(0,4);
  const budgetCap = (s.match(/under \$?(\d+)/)||[])[1];
  const cards=(list,pre)=>{
    let L=list;
    if(budgetCap){ L=list.filter(w=>w.price<=+budgetCap); if(!L.length) L=list.slice(0,3); }
    return {t:pre, list:L.slice(0,3)};
  };
  if(/pottery|clay|ceramic|mug/.test(s)&&/date|romantic|anniversary|partner|girlfriend|boyfriend|wife|husband/.test(s))
    return cards(pick(['pottery-mug','truffles','pasta']),`A pottery date — excellent taste 🏺 For <b>this weekend</b>: <i>${WORKSHOPS[0].title}</i> has ${WORKSHOPS[0].slots[0].left} spots left Saturday morning. Want cozy after? Chocolate truffles Saturday afternoon, or pasta + Chianti Friday night.`);
  if(/pottery|clay|ceramic/.test(s)) return cards(pick(['pottery-mug','pottery-intensive','terrarium']),`Clay people are my favorite people. The <b>morning mug class</b> is the perfect first touch of the wheel (${WORKSHOPS[0].slots[0].d}, ${WORKSHOPS[0].slots[0].left} spots left). Feeling ambitious? The <b>two-day intensive</b> in November leaves you with 6–8 finished pieces.`);
  if(/pumpkin|halloween|fall|autumn|october|harvest/.test(s)) return cards(pick(['pumpkin-carve','fall-garden','watercolor']),`October energy 🎃 <b>Pumpkin Pick &amp; Carve</b> at Harvest Hill Farm is peak fall — wagon ride, endless cider, any pumpkin you can carry. Pair it with the <b>fall garden bulbs</b> class Saturday morning for a full harvest day.`);
  if(/pasta|italian|pizza|cook|dinner/.test(s)) return cards(pick(['pasta','sourdough','croissant']),`Mangia! <b>Handmade Pasta</b> in the North End ends with you eating everything plus Chianti — Friday 6:30pm has ${WORKSHOPS[2].slots[0].left} spots. Or go full carb weekend: sourdough Sunday morning.`);
  if(/knit|yarn|scarf|crochet|cozy/.test(s)) return cards(pick(['knit-scarf','fall-garden','terrarium','salsa']),`Cozy season is here 🧶 <b>Knit Your First Scarf</b> Wednesday evenings in JP — tea, cocoa, and you leave with needles + yarn. Under $50, zero experience needed.`);
  if(/chocolate|truffle|sweet|dessert/.test(s)) return cards(pick(['truffles','croissant','pasta']),`Say less 🍫 <b>Chocolate Truffles &amp; Bonbons</b> — 24 truffles, gift-boxed, Saturday 3pm. The Friday evening slot is smaller and extra romantic, just saying.`);
  if(/dance|salsa|move|party/.test(s)) return cards(pick(['salsa','dance','pottery-mug']),`Friday night = <b>Beginner Salsa Night</b> in East Boston 💃 No partner needed, open social dancing after the lesson, first-drink token included.`);
  if(/garden|plant|terrarium|flower/.test(s)) return cards(pick(['terrarium','fall-garden','wreath']),`Green thumb loading 🌱 <b>Terrarium building</b> Sunday (1.5 hrs, take home a living world) or <b>Fall Garden bulbs</b> Saturday — plant now, thank yourself in April.`);
  if(/kid|family|child|toddler/.test(s)) return cards(pick(['pumpkin-carve','terrarium','fall-garden']),`Family-approved 👨‍👩‍👧 <b>Pumpkin Pick &amp; Carve</b> — kids under 5 free, safe carving tools, farm animals. <b>Terrarium building</b> is also a hit with little hands.`);
  if(/cheap|budget|under \$?50|affordable/.test(s)) return cards(pick(['salsa','pumpkin-carve','fall-garden']),`Ballin&rsquo; on a budget 💛 Great news — the best stuff is cheap: <b>Salsa Night</b> ($30), <b>Pumpkin Pick &amp; Carve</b> ($28), <b>Fall Garden</b> ($40). All top-rated.`);
  if(/weekend|this week|saturday|sunday/.test(s)) return cards(pick(['pottery-mug','pasta','pumpkin-carve']),`This weekend&rsquo;s hottest picks 🔥 <b>Pottery Saturday 10am</b> (${WORKSHOPS[0].slots[0].left} left), <b>Pasta Friday 6:30pm</b> (${WORKSHOPS[2].slots[0].left} left), <b>Pumpkin farm</b> both days. What vibe — hands, taste buds, or outdoors?`);
  if(/date|romantic/.test(s)) return cards(pick(['truffles','pasta','pottery-mug']),`Date night, upgraded 💛 My top three: <b>truffles Friday evening</b> (small class, gift boxes), <b>pasta + Chianti</b>, or <b>pottery at golden hour</b>. Can&rsquo;t lose.`);
  if(/hi|hello|hey/.test(s)) return {t:`Hey! ☀️ Give me a vibe — <i>crafty, tasty, outdoorsy, date-y</i> — and a day, and I&rsquo;ll find your workshop.`};
  return cards(WORKSHOPS.slice().sort((a,b)=>b.rating*b.rc-a.rating*a.rc).slice(0,3),`Hmm, let me read between the lines… 🔍 Based on what&rsquo;s loved right now, these three are can&rsquo;t-miss. Or try me with something like <i>&ldquo;cozy and under $50&rdquo;</i>.`);
}
function miniCards(list){
  return `<div class="mini">${list.map(w=>`<a class="mini-card" data-go="#/w/${w.id}" href="#/w/${w.id}"><span class="th">${scene(w.motif,w.title)}</span><span><span class="mt">${esc(w.title)}</span><span class="ms" style="display:block">${money(w.price)} · ${w.dur} · ${w.dist} mi · ★${w.rating}</span></span></a>`).join('')}</div>`;
}
function vSunny(){
  return `
  <div class="topbar"><div class="topbar-in"><div class="topbar-title">☀️ Sunny <span style="font-size:12px;color:var(--muted);font-family:var(--sans)">AI concierge</span></div></div></div>
  <div class="sunny-hello"><span class="sic"><svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#FFF6E8" stroke-width="2"><circle cx="12" cy="12" r="4.5" fill="#FFF6E8" stroke="none"/><path d="M12 2.5v2.6M12 18.9v2.6M2.5 12h2.6M18.9 12h2.6M5.3 5.3l1.8 1.8M16.9 16.9l1.8 1.8M18.7 5.3l-1.8 1.8M7.1 16.9l-1.8 1.8"/></svg></span>
  <div><b>Not sure what to do this weekend?</b><br><span style="font-size:13.5px;color:var(--ink-soft)">Describe the vibe — I&rsquo;ll match you to real workshops with open spots.</span></div></div>
  <div class="suggest" id="sugg">${['Plan a pottery date this weekend','Something cozy under $50','Pumpkin picking with kids','Date night idea'].map(s=>`<button class="chip" data-s="${esc(s)}">${s}</button>`).join('')}</div>
  <div class="chat" id="chat">${CHAT.map(m=>`<div class="msg ${m.me?'me':'sunny'}">${m.t}${m.list?miniCards(m.list):''}</div>`).join('')}</div>
  <div style="height:76px"></div>
  <div class="chatbar"><div class="chatbar-in"><input id="cin" placeholder="Ask Sunny anything…" aria-label="Ask Sunny"><button class="sendbtn" id="csend" aria-label="Send">➤</button></div></div>`;
}
function bindSunny(){
  const chat=document.getElementById('chat'), cin=document.getElementById('cin');
  const send=(text)=>{
    const q=(text||cin.value).trim(); if(!q) return;
    CHAT.push({me:true,t:esc(q)}); cin.value='';
    chat.insertAdjacentHTML('beforeend',`<div class="msg me">${esc(q)}</div>`);
    const tp=document.createElement('div'); tp.className='msg sunny typing'; tp.innerHTML='<i></i><i></i><i></i>';
    setTimeout(()=>{ chat.appendChild(tp); tp.scrollIntoView({behavior:'smooth',block:'end'}); },250);
    setTimeout(()=>{ const r=sunnyReply(q); CHAT.push({me:false,t:r.t,list:r.list});
      tp.outerHTML=`<div class="msg sunny">${r.t}${r.list?miniCards(r.list):''}</div>`;
      chat.lastElementChild.scrollIntoView({behavior:'smooth',block:'end'}); }, 1400);
    chat.lastElementChild.scrollIntoView({behavior:'smooth',block:'nearest'});
  };
  document.getElementById('csend').onclick=()=>send();
  cin.addEventListener('keydown',e=>{ if(e.key==='Enter') send(); });
  document.getElementById('sugg').addEventListener('click',e=>{ const b=e.target.closest('[data-s]'); if(b) send(b.dataset.s); });
}

/* ---------- CALENDAR ---------- */
let CALM='oct';
function vCalendar(){
  const se=SEASONS[CALM];
  const list=WORKSHOPS.filter(w=>w.months.includes(CALM));
  return `
  <div class="topbar"><div class="topbar-in"><div class="topbar-title">Seasonal calendar</div></div></div>
  <div class="monthtabs">${Object.entries(SEASONS).map(([k,v])=>`<button class="monthtab${k===CALM?' on':''}" data-m="${k}">${v.name}</button>`).join('')}</div>
  <div class="cal-hero grain">${scene(se.motif,se.theme)}<div class="txt"><h2 class="serif">${esc(se.theme)}</h2><p>${esc(se.sub)}</p></div></div>
  <div class="cal-day"><div class="dlabel">Seasonal workshops · ${list.length}</div>
    <div class="cards">${list.map(wcard).join('')}</div></div>
  <div class="foot-note">Seasons change, so does the calendar.<br>Winter Magic drops December 1st ❄️</div>`;
}
function bindCalendar(){
  document.querySelectorAll('[data-m]').forEach(b=>b.onclick=()=>{CALM=b.dataset.m; rerender();});
}

/* ============================== DISPATCH ============================== */
let lastBind=null;
function route(){
  const h=(location.hash||'#/discover').replace(/^#\//,'');
  const [name,arg]=h.split('/');
  let html='', tab=name, bind=null;
  if(name==='discover'){ html=vDiscover(); bind=bindDiscover; }
  else if(name==='w'){ html=vDetail(arg); tab='discover'; bind=()=>bindDetail(arg); }
  else if(name==='book'){ html=vBook(arg); tab='discover'; bind=()=>bindBook(arg); }
  else if(name==='pay'){ html=vPay(arg); tab='discover'; bind=()=>bindPay(arg); }
  else if(name==='done'){ html=vDone(arg); tab='discover'; bind=bindDone; }
  else if(name==='feed'){ html=vFeed(); bind=bindFeed; }
  else if(name==='sunny'){ html=vSunny(); bind=bindSunny; }
  else if(name==='calendar'){ html=vCalendar(); bind=bindCalendar; }
  else if(name==='host'){ html=vHost(); tab='host'; bind=bindHost; }
  else { html=vDiscover(); bind=bindDiscover; tab='discover'; }
  renderTabs(tab);
  view.classList.remove('enter'); void view.offsetWidth; view.classList.add('enter');
  view.innerHTML=html;
  window.scrollTo(0,0);
  if(bind) bind();
  lastBind=bind;
}
function rerender(keepBind=true){
  const pos=window.scrollY;
  const h=location.hash;
  route();
  if(!keepBind) window.scrollTo(0,pos);
}
window.addEventListener('hashchange', route);
if(!location.hash) location.hash='#/discover';
route();
