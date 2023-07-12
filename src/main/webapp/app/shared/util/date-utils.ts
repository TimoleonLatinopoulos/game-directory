export const greekDateToEng = (dateString: string | undefined): string | undefined => {
  let result = dateString;
  if (result !== undefined) {
    result = result.replace('Ιαν', 'Jan');
    result = result.replace('Φεβ', 'Feb');
    result = result.replace('Μαρ', 'Mar');
    result = result.replace('Απρ', 'Apr');
    result = result.replace('Μαι', 'May');
    result = result.replace('Iουν', 'Jun');
    result = result.replace('Iουλ', 'Jul');
    result = result.replace('Αυγ', 'Aug');
    result = result.replace('Σεπ', 'Sep');
    result = result.replace('Οκτ', 'Oct');
    result = result.replace('Νοε', 'Nov');
    result = result.replace('Δεκ', 'Dec');
  }
  return result;
};

export const getDateWithTimeOffset = (dateString: any): Date => {
  const date = new Date(dateString);
  date.setTime(date.getTime() - date.getTimezoneOffset() * 60000);

  return date;
};
