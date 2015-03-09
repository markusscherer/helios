person_notlikesdate(xxx,xxx,xxx).
person_likesdate(xxx,xxx,xxx).
person_likesday(xxx,xxx,xxx).
person_notlikesday(xxx,xxx,xxx).
person_friend(xxx,xxx).
person_notfriend(xxx,xxx).

person_validdate(X, DATE, SHIFT) :-
	\+ person_likesdate(X,_,_),
	\+ person_notlikesdate(X,DATE,SHIFT).

person_validdate(X, DATE, SHIFT) :-
	person_likesdate(X,DATE,SHIFT).

person_validday(X,DAY,SHIFT) :-
	\+ person_likesday(X,_,_),
	\+ person_notlikesday(X,DAY,SHIFT).

person_validday(X,DAY,SHIFT) :-
	person_likesday(X,DAY,SHIFT).

person_validpartner(X,PARTNER) :-
	\+ person_friend(X,_),
	\+ person_notfriend(X,PARTNER).

person_validpartner(X,PARTNER) :-
	person_friend(X,PARTNER).

neff_nefs_date(NEFF, NEFS, DATE) :-
	date_day_shift(DATE,DAY,SHIFT),
	neff(NEFF),
	nefs(NEFS),
	NEFF \= NEFS,
	person_validdate(NEFF,DATE, SHIFT),
	person_validdate(NEFS, DATE, SHIFT),
	person_validday(NEFF, DAY, SHIFT),
	person_validday(NEFS, DAY, SHIFT),
	person_validpartner(NEFF, NEFS),
	person_validpartner(NEFS, NEFF).

rtwf_rtws_date(RTWF, RTWS, DATE) :-
	date_day_shift(DATE,DAY,SHIFT),
	rtwf(RTWF),
	rtws(RTWS),
	RTWF \= RTWS,
	person_validdate(RTWF,DATE, SHIFT),
	person_validdate(RTWS, DATE, SHIFT),
	person_validday(RTWF, DAY, SHIFT),
	person_validday(RTWS, DAY, SHIFT),
	person_validpartner(RTWF, RTWS),
	person_validpartner(RTWS, RTWF).
	
stbf_stbs_date(STBF, STBS, DATE) :-
	date_day_shift(DATE,DAY,SHIFT),
	stbf(STBF),
	stbs(STBS),
	STBF \= STBS,
	person_validdate(STBF,DATE, SHIFT),
	person_validdate(STBS, DATE, SHIFT),
	person_validday(STBF, DAY, SHIFT),
	person_validday(STBS, DAY, SHIFT),
	person_validpartner(STBF, STBS),
	person_validpartner(STBS, STBF).