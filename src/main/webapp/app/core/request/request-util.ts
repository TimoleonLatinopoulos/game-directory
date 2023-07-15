import { HttpParams } from '@angular/common/http';

export const createRequestOption = (req?: any): HttpParams => {
  let options: HttpParams = new HttpParams();

  if (req) {
    Object.keys(req).forEach(key => {
      if (key !== 'sort' && req[key] !== undefined) {
        for (const value of [].concat(req[key]).filter(v => v !== '')) {
          options = options.append(key, value);
        }
      }
    });

    if (req.sort) {
      req.sort.forEach((val: string) => {
        options = options.append('sort', val);
      });
    }
  }

  return options;
};

export const createSearchRequestOption = (req?: any): HttpParams => {
  let options: HttpParams = new HttpParams();
  if (req) {
    Object.keys(req).forEach(key => {
      if (key !== 'sort' && key !== 'filter') {
        options = options.set(key, req[key]);
      }
    });
    if (req.sort) {
      options = options.set('sort', JSON.stringify(req.sort));
    }
    if (req.filter) {
      options = options.set('filter', JSON.stringify(req.filter));
    }
  }
  return options;
};
