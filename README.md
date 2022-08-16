# Networth Tracker

Networth Tracker is a Spring boot API for storing everyday Portfolio Value automatically

## API Base Path

Use the link [valuetracker](https://valuetracker.herokuapp.com/) to use the API


## API Usage
Javascript Example

```javascript
// returns a Company Stock Price and Volume
axios.get(`https://valuetracker.herokuapp.com/{company_name}`)

// Downloadable Company Ticker List in txt format
axios.get(`https://valuetracker.herokuapp.com/tickerlist`)

// Add new Tickers in the Ticker List
axios.get(`https://valuetracker.herokuapp.com/{company_name}/{ticker}`)

// returns Portfolio List
axios.get(`https://valuetracker.herokuapp.com/portfoliolist`)

// returns Networth List
axios.get(`https://valuetracker.herokuapp.com/networthlist`)
```
## Implementation
Implemented the API in a React Project : [equityTracker](https://trackequity.herokuapp.com/)

[Github](https://github.com/manojeet24/NetworthTrackerApp/tree/master)


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
[Manojeet Das](https://www.linkedin.com/in/manojeet-das-16005a162/)
