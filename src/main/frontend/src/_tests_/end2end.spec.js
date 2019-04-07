describe('plan a friendly Match', () => {
  beforeAll(async () => {
    await page.goto('http://localhost:8080');
  });

  it('should find Planning equipe soccer 5, agenda and players nav title', async () => {
    await expect(page).toMatch('Planning Equipe Soccer 5');
    await expect(page).toMatch('Agenda');
    await expect(page).toMatch('Players');
  });

  it('should select 13 players', async () => {
    const matchDate = new Date().toISOString().slice(0, 10);
    await expect(page).toClick('a', { text: 'Agenda' })
    await expect(page).toClick('button', { text: 'ADD' })
    await expect(page).toMatch(matchDate)
    await expect(page).toClick('span[data-test="0-input"] input')
    await expect(page).toClick('span[data-test="1-input"] input')
    await expect(page).toClick('span[data-test="2-input"] input')
    await expect(page).toClick('span[data-test="3-input"] input')
    await expect(page).toClick('span[data-test="4-input"] input')
    await expect(page).toClick('span[data-test="5-input"] input')
    await expect(page).toClick('span[data-test="6-input"] input')
    await expect(page).toClick('span[data-test="7-input"] input')
    await expect(page).toClick('span[data-test="8-input"] input')
    await expect(page).toClick('span[data-test="9-input"] input')
    await expect(page).toClick('span[data-test="10-input"] input')
    await expect(page).toClick('span[data-test="11-input"] input')
    await expect(page).toClick('span[data-test="12-input"] input')

    await page.waitForSelector('[data-test="number-of-players"]')
    let numberOfPlayers = await page.$eval('[data-test="number-of-players"]', e => e.innerHTML);
    expect(numberOfPlayers).toEqual("13");
  })

  it('should plan the match', async() => {
    await expect(page).toClick('a', {text: 'Planning Equipe Soccer 5'})
    const matchDate = new Date().toISOString().slice(0, 10);
    await page.waitForSelector('[data-test="matches-to-plan"]')
    // await jestPuppeteer.debug()
    await expect(page).toMatch('Matches to plan')
    await expect(page).toMatch(matchDate)
    await expect(page).toClick('button', {text: 'PLAN'})
    await page.waitForSelector('[data-test="next-matches"]')
    await expect(page).toMatch('Next friendly match')
    const playerCounts = await page.$$eval('[data-test="player"]', divs => divs.length);
    await expect(playerCounts).toEqual(10);
  })
});