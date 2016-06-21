#!/usr/bin/perl
use strict;
use warnings;
use DBI;
use DirHandle;
use FileHandle;
use Carp;
use Encode;
#alex
sub FileToDb($$$$$);
sub GetTabs($);
sub GetLines($);
sub GetFiles($);
sub ParseFile($$);
my $str="good 234 agfda 432 fgasgf  gareg 434";
#$str=~tr#a-z#n-zam#;
my @numms=$str=~/\d+/g;
print "$1\n" if $str=~/(\d+) /g;
print "$1\n" if $str=~/\G(\w+)/;
print "$1\n" if $str=~/(\w+) /g;
my $dir="/media/psf/Host/Volumes/C/alex/factors/Dates";
my @_types = qw(DATE DATETIME INTEGER DOUBLE VARCHAR);
my %_types; @_types{@_types} = (0..$#_types);
my $_bom = chr(239) . chr(187) . chr(191); # Byte Order Mark: <U+FEFF>
my $_default_varchar_len = 20;
{
    my ($data_source, $username, $password) = ("dbi:mysql:factors", "alex", "aleko");
    my $opt = {RaiseError => 1, AutoCommit => 0};
    my $dbh = DBI->connect($data_source, $username, $password, $opt) or die $DBI::errstr;
    my $tabs = GetTabs($dbh); my %tabs; @tabs{@$tabs} = ();
    print "tabs: @$tabs\n";
    my @datesfileArray=ParseFile($dir,"dates.csv");
    my $files=GetFiles($dir);
    for my $fnum(@$files) {
	if($fnum !~/dates\.csv/){
	 my @fileHashArray=ParseFile($dir,$fnum);
	 FileToDb($fnum,@fileHashArray,@datesfileArray,$dbh,\%tabs);
	}
    }
    $dbh->disconnect or warn $dbh->errstr;
}
sub FileToDb($$$$$){
    my ($fname,$fileRef,$dateRef,$dbh,$tabs) = @_;
    my @stm;
    my $specs="ticker varchar(20),year DATE,quarter varchar(2),date DATE,value FLOAT";
    my @name=split(/\./,$fname);
    my $tab=$name[0];
    if(exists $tabs->{$tab}) {
	push @stm, "DROP TABLE $tab;";
    }
    push @stm, "CREATE TABLE $tab($specs);";
    for my $stm(@stm) {
	print "$stm\n";
	$dbh->do($stm) or die $dbh->errstr;
    }
    my @fields = split(/,/, $specs);
    my %fields; @fields{@fields} = (0..$#fields);
    my $nf = @fields;
    my $csvfields = join(",", @fields);
    my $placeholders = join(",", map {"?"} @fields);
    my $sth = $dbh->prepare("INSERT INTO $tab ($csvfields) VALUES ($placeholders)") or die $dbh->errstr;
    for my $lnum($#$dateRef) {
	my $dateLineRef=$dateRef->[$lnum];
	my $val=$dateLineRef->[0];
    }
}
sub GetFiles($)
{
    my ($dir) = @_;
    my $dh = new DirHandle($dir) or die "ERR $dir: $!";
    my @files;
    while(defined(my $file = $dh->read)) {
        next if $file =~ /^\./;
	push @files, $file;
    }
    @files = sort @files;
    return \@files;
}
sub GetTabs($)
{
    my $dbh = shift;
    my $rows = $dbh->selectall_arrayref("SHOW TABLES;") or die $dbh->errstr;
    my @tabs;
    for(@$rows) {
        @$_ == 1 or die "ERR @$_";
        push @tabs, @$_;
    }
    return \@tabs;
}



sub ParseFile($$)
{
    my ($dir, $file) = @_;
    my @fileArray=();
    my $lines = GetLines("$dir/$file");
    my $years = GetYears($lines->[0]);
    my $n = @$lines - 1;
    print "    $file $n...\n";
    my $lineno = -1;
    for my $line(@$lines) {
	$lineno++;
	next if ((0 == $lineno) or (1 == $lineno)); # skip header
	my @line=split(',',$line);
	$line[0]=~/^([a-zA-Z]+)/m;
	my $ticker=$1;
	if($line[2]=~/[1-9A-Za-z]/m){
	push @{$fileArray[$lineno-2]}, $ticker;
	push @{$fileArray[$lineno-2]}, $ticker;
	}
	for my $col (2..$#line){
	    if($line[$col] =~/[1-9A-Za-z]/){
		push @{$fileArray[$lineno-2]}, $line[$col];
	    }
	}
    }
    return @fileArray;
}


sub GetYears($)
{
    my $line = shift;
    my @line=split(',',$line);
    my $row = 0;
    my @years=();
    foreach my $i (1..2){
	push @{$years[$row]}, -1;
	push @{$years[$row]}, -1;
	$row++;
    }
    foreach my $column (@line)
    {
	if($column =~ /^\d{4}$/){
	    foreach my $i (1..4){
		push @{$years[$row]}, $column;
		push @{$years[$row]}, "Q$i";
		$row++;
	    }
	}
    }
    return \@years;
}
sub GetLines($)
{
    my $path = shift;
    my $fh;
    if($path =~ /\.gz$/) {
        $fh = new FileHandle("gunzip -c $path|") or confess "ERR $path";
    } else {
        $fh = new FileHandle($path) or confess "ERR $path";
    }
    my @lines;
    while(<$fh>) {
        push @lines, $_;
    }
    return \@lines;
}
